package com.squires.gamechanger.leagues

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squires.gamechanger.common.Result
import com.squires.gamechanger.common.UiState
import com.squires.gamechanger.domain.usecase.GetLeaguesUseCase
import com.squires.gamechanger.domain.usecase.SearchLeaguesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class LeaguesViewModel @Inject constructor(
    private val getLeaguesUseCase: GetLeaguesUseCase,
    private val searchLeaguesUseCase: SearchLeaguesUseCase,
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _uiState = MutableStateFlow<LeaguesUiState>(UiState.Loading)
    val uiState: StateFlow<LeaguesUiState> = _uiState.asStateFlow()

    private val queryTrigger = MutableSharedFlow<String>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    init {
        viewModelScope.launch {
            queryTrigger
                .onStart { emit("") }
                .debounce { if (it.isBlank()) 0L else DEBOUNCE_MILLIS }
                .flatMapLatest { query ->
                    if (query.isBlank()) {
                        getLeaguesUseCase().map { result ->
                            when (result) {
                                is Result.Loading -> UiState.Loading
                                is Result.Success -> UiState.Success(result.data)
                                is Result.Error -> UiState.Error(result.message, result.cachedData)
                            }
                        }
                    } else {
                        flow<LeaguesUiState> {
                            emit(UiState.Loading)
                            emit(
                                when (val result = searchLeaguesUseCase(query)) {
                                    is Result.Success -> UiState.Success(result.data)
                                    is Result.Error -> UiState.Error(result.message)
                                    is Result.Loading -> UiState.Loading // unreachable: searchLeaguesUseCase is a one-shot suspend fun
                                }
                            )
                        }
                    }
                }
                .collect { _uiState.value = it }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        queryTrigger.tryEmit(query)
    }

    fun retry() {
        queryTrigger.tryEmit(_searchQuery.value)
    }

    companion object {
        private const val DEBOUNCE_MILLIS = 300L
    }
}
