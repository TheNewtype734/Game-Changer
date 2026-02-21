package com.squires.gamechanger.teamdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squires.gamechanger.common.Result
import com.squires.gamechanger.common.UiState
import com.squires.gamechanger.domain.usecase.GetTeamDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TeamDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getTeamDetailUseCase: GetTeamDetailUseCase,
) : ViewModel() {

    private val teamId: String = checkNotNull(savedStateHandle[TeamDetailArgs.TEAM_ID])

    val uiState: StateFlow<TeamDetailUiState> = getTeamDetailUseCase(teamId)
        .map { result ->
            when (result) {
                is Result.Loading -> UiState.Loading
                is Result.Success -> UiState.Success(result.data)
                is Result.Error -> UiState.Error(result.message)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState.Loading,
        )
}

object TeamDetailArgs {
    const val TEAM_ID = "teamId"
}
