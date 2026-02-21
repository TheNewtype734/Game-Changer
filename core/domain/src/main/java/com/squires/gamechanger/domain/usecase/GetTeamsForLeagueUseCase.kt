package com.squires.gamechanger.domain.usecase

import com.squires.gamechanger.common.Result
import com.squires.gamechanger.domain.model.Team
import com.squires.gamechanger.domain.repository.TeamRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTeamsForLeagueUseCase @Inject constructor(
    private val teamRepository: TeamRepository,
) {
    operator fun invoke(leagueName: String): Flow<Result<List<Team>>> =
        teamRepository.getTeamsForLeague(leagueName)
}
