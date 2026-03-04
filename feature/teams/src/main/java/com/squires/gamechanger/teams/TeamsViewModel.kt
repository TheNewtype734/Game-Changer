package com.squires.gamechanger.teams

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.squires.gamechanger.common.Result
import com.squires.gamechanger.domain.model.Team
import com.squires.gamechanger.domain.usecase.GetTeamsForLeaguePagedUseCase
import com.squires.gamechanger.domain.usecase.HasTeamsForLeagueUseCase
import com.squires.gamechanger.domain.usecase.RefreshTeamsForLeagueUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTeamsForLeaguePagedUseCase: GetTeamsForLeaguePagedUseCase,
    private val hasTeamsForLeagueUseCase: HasTeamsForLeagueUseCase,
    private val refreshTeamsForLeagueUseCase: RefreshTeamsForLeagueUseCase,
) : ViewModel() {

    val leagueName: String = checkNotNull(savedStateHandle[TeamsRoute::leagueName.name])

    private val _refreshState = MutableStateFlow<Result<Unit>>(Result.Success(Unit))
    val refreshState: StateFlow<Result<Unit>> = _refreshState.asStateFlow()

    val pagedTeams: Flow<PagingData<Team>> = getTeamsForLeaguePagedUseCase(leagueName)
        .cachedIn(viewModelScope)

    init { triggerRefresh() }

    fun retry() { triggerRefresh() }

    private fun triggerRefresh() {
        viewModelScope.launch {
            if (!hasTeamsForLeagueUseCase(leagueName)) {
                _refreshState.value = Result.Loading
            }
            _refreshState.value = refreshTeamsForLeagueUseCase(leagueName)
        }
    }
}
