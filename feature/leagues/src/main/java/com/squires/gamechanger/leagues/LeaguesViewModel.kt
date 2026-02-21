package com.squires.gamechanger.leagues

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squires.gamechanger.common.Result
import com.squires.gamechanger.common.UiState
import com.squires.gamechanger.domain.usecase.GetLeaguesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LeaguesViewModel @Inject constructor(
    getLeaguesUseCase: GetLeaguesUseCase,
) : ViewModel() {

    val uiState: StateFlow<LeagueUiState> = getLeaguesUseCase()
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
