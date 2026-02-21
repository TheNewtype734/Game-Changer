package com.squires.gamechanger.domain.usecase

import com.squires.gamechanger.common.Result
import com.squires.gamechanger.domain.model.League
import com.squires.gamechanger.domain.repository.LeagueRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLeaguesUseCase @Inject constructor(
    private val leagueRepository: LeagueRepository,
) {
    operator fun invoke(): Flow<Result<List<League>>> = leagueRepository.getLeagues()
}
