package com.squires.gamechanger.domain.usecase

import com.squires.gamechanger.common.Result
import com.squires.gamechanger.domain.repository.LeagueRepository
import javax.inject.Inject

class RefreshLeaguesUseCase @Inject constructor(
    private val leagueRepository: LeagueRepository,
) {
    suspend operator fun invoke(): Result<Unit> = leagueRepository.refreshLeagues()
}
