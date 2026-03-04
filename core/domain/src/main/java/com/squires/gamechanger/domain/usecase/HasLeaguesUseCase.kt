package com.squires.gamechanger.domain.usecase

import com.squires.gamechanger.domain.repository.LeagueRepository
import javax.inject.Inject

class HasLeaguesUseCase @Inject constructor(
    private val leagueRepository: LeagueRepository,
) {
    suspend operator fun invoke(): Boolean = leagueRepository.hasLeagues()
}
