package com.squires.gamechanger.teamdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squires.gamechanger.common.Result
import com.squires.gamechanger.common.UiState
import com.squires.gamechanger.domain.usecase.GetTeamDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTeamDetailUseCase: GetTeamDetailUseCase,
) : ViewModel() {

    private val teamId: String = checkNotNull(savedStateHandle[TeamDetailRoute::teamId.name])

    private val _uiState = MutableStateFlow<TeamDetailUiState>(UiState.Loading)
    val uiState: StateFlow<TeamDetailUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadTeamDetail()
    }

    fun retry() {
        loadTeamDetail()
    }

    private fun loadTeamDetail() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            getTeamDetailUseCase(teamId)
                .map { result ->
                    when (result) {
                        is Result.Loading -> UiState.Loading
                        is Result.Success -> UiState.Success(result.data)
                        is Result.Error -> UiState.Error(result.message)
                    }
                }
                .collect { _uiState.value = it }
        }
    }
}
