package com.squires.gamechanger.domain.usecase

import androidx.paging.PagingData
import com.squires.gamechanger.domain.model.League
import com.squires.gamechanger.domain.repository.LeagueRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLeaguesPagedUseCase @Inject constructor(
    private val leagueRepository: LeagueRepository,
) {
    operator fun invoke(): Flow<PagingData<League>> = leagueRepository.getLeaguesPaged()
}
