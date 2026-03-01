package com.squires.gamechanger.domain.usecase

import com.squires.gamechanger.common.Result
import com.squires.gamechanger.domain.model.League
import com.squires.gamechanger.domain.repository.LeagueRepository
import javax.inject.Inject

class SearchLeaguesUseCase @Inject constructor(
    private val leagueRepository: LeagueRepository,
) {
    suspend operator fun invoke(query: String): Result<List<League>> =
        leagueRepository.searchLeagues(query)
}
