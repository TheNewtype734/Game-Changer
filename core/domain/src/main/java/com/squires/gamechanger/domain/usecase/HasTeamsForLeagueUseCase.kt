package com.squires.gamechanger.domain.usecase

import com.squires.gamechanger.domain.repository.TeamRepository
import javax.inject.Inject

class HasTeamsForLeagueUseCase @Inject constructor(
    private val teamRepository: TeamRepository,
) {
    suspend operator fun invoke(leagueName: String): Boolean =
        teamRepository.hasTeamsForLeague(leagueName)
}
