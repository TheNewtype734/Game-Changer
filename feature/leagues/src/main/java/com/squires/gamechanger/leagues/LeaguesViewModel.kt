package com.squires.gamechanger.leagues

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.squires.gamechanger.common.Result
import com.squires.gamechanger.common.UiState
import com.squires.gamechanger.domain.model.League
import com.squires.gamechanger.domain.usecase.GetLeaguesPagedUseCase
import com.squires.gamechanger.domain.usecase.RefreshLeaguesUseCase
import com.squires.gamechanger.domain.usecase.SearchLeaguesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class LeaguesViewModel @Inject constructor(
    private val getLeaguesPagedUseCase: GetLeaguesPagedUseCase,
    private val refreshLeaguesUseCase: RefreshLeaguesUseCase,
    private val searchLeaguesUseCase: SearchLeaguesUseCase,
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _refreshState = MutableStateFlow<Result<Unit>>(Result.Loading)
    val refreshState: StateFlow<Result<Unit>> = _refreshState.asStateFlow()

    private val _searchState = MutableStateFlow<LeaguesUiState>(UiState.Loading)
    val searchState: StateFlow<LeaguesUiState> = _searchState.asStateFlow()

    val pagedLeagues: Flow<PagingData<League>> = getLeaguesPagedUseCase()
        .cachedIn(viewModelScope)

    private val queryTrigger = MutableSharedFlow<String>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    init {
        triggerRefresh()
        viewModelScope.launch {
            queryTrigger
                .debounce { if (it.isBlank()) 0L else DEBOUNCE_MILLIS }
                .filter { it.isNotBlank() }
                .flatMapLatest { query ->
                    flow<LeaguesUiState> {
                        emit(UiState.Loading)
                        emit(
                            when (val result = searchLeaguesUseCase(query)) {
                                is Result.Success -> UiState.Success(result.data)
                                is Result.Error -> UiState.Error(result.message)
                                is Result.Loading -> UiState.Loading // unreachable: searchLeaguesUseCase is a one-shot suspend fun
                            },
                        )
                    }
                }
                .collect { _searchState.value = it }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        queryTrigger.tryEmit(query)
    }

    fun retry() {
        if (_searchQuery.value.isBlank()) triggerRefresh()
        else queryTrigger.tryEmit(_searchQuery.value)
    }

    private fun triggerRefresh() {
        viewModelScope.launch {
            _refreshState.value = Result.Loading
            _refreshState.value = refreshLeaguesUseCase()
        }
    }

    companion object {
        private const val DEBOUNCE_MILLIS = 300L
    }
}
