package com.squires.gamechanger.domain.usecase

import androidx.paging.PagingData
import com.squires.gamechanger.domain.model.Team
import com.squires.gamechanger.domain.repository.TeamRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTeamsForLeaguePagedUseCase @Inject constructor(
    private val teamRepository: TeamRepository,
) {
    operator fun invoke(leagueName: String): Flow<PagingData<Team>> =
        teamRepository.getTeamsForLeaguePaged(leagueName)
}
