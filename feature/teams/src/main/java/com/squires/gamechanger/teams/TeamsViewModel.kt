package com.squires.gamechanger.teams

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squires.gamechanger.common.Result
import com.squires.gamechanger.common.UiState
import com.squires.gamechanger.domain.usecase.GetTeamsForLeagueUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTeamsForLeagueUseCase: GetTeamsForLeagueUseCase,
) : ViewModel() {

    val leagueName: String = checkNotNull(savedStateHandle[TeamsRoute::leagueName.name])

    private val _uiState = MutableStateFlow<TeamsUiState>(UiState.Loading)
    val uiState: StateFlow<TeamsUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadTeams()
    }

    fun retry() {
        loadTeams()
    }

    private fun loadTeams() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            getTeamsForLeagueUseCase(leagueName)
                .map { result ->
                    when (result) {
                        is Result.Loading -> UiState.Loading
                        is Result.Success -> UiState.Success(result.data)
                        is Result.Error -> UiState.Error(result.message, result.cachedData)
                    }
                }
                .collect { _uiState.value = it }
        }
    }
}
