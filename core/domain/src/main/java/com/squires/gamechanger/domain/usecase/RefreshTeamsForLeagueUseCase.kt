package com.squires.gamechanger.domain.usecase

import com.squires.gamechanger.common.Result
import com.squires.gamechanger.domain.repository.TeamRepository
import javax.inject.Inject

class RefreshTeamsForLeagueUseCase @Inject constructor(
    private val teamRepository: TeamRepository,
) {
    suspend operator fun invoke(leagueName: String): Result<Unit> = teamRepository.refreshTeamsForLeague(leagueName)
}
