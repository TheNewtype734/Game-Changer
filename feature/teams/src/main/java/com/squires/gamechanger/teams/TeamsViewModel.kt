package com.squires.gamechanger.teams

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squires.gamechanger.common.Result
import com.squires.gamechanger.common.UiState
import com.squires.gamechanger.domain.usecase.GetTeamsForLeagueUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TeamsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getTeamsForLeagueUseCase: GetTeamsForLeagueUseCase,
) : ViewModel() {

    val leagueName: String = checkNotNull(savedStateHandle[TeamsArgs.LEAGUE_NAME])

    val uiState: StateFlow<TeamsUiState> = getTeamsForLeagueUseCase(leagueName)
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

object TeamsArgs {
    const val LEAGUE_NAME = "leagueName"
}
