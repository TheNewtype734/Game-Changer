package com.squires.gamechanger.domain.repository

import com.squires.gamechanger.common.Result
import com.squires.gamechanger.domain.model.League
import kotlinx.coroutines.flow.Flow

interface LeagueRepository {
    fun getLeagues(): Flow<Result<List<League>>>
    suspend fun searchLeagues(query: String): Result<List<League>>
    suspend fun refreshLeagues(): Result<Unit>
}
