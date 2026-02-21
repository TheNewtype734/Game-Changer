package com.squires.gamechanger.data.repository

import com.squires.gamechanger.common.Result
import com.squires.gamechanger.data.local.dao.LeagueDao
import com.squires.gamechanger.data.mapper.toDomain
import com.squires.gamechanger.data.mapper.toEntity
import com.squires.gamechanger.domain.model.League
import com.squires.gamechanger.domain.repository.LeagueRepository
import com.squires.gamechanger.network.api.SportsDbApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class LeagueRepositoryImpl @Inject constructor(
    private val api: SportsDbApi,
    private val dao: LeagueDao,
) : LeagueRepository {

    override fun getLeagues(): Flow<Result<List<League>>> {
        return dao.getLeagues()
            .map<_, Result<List<League>>> { entities ->
                Result.Success(entities.map { it.toDomain() })
            }
            .onStart {
                emit(Result.Loading)
                refreshLeagues()
            }
            .catch { throwable ->
                emit(Result.Error(throwable.message ?: "Unknown error", throwable))
            }
    }

    private suspend fun refreshLeagues() {
        runCatching {
            val response = api.getAllLeagues()
            val entities = response.leagues?.mapNotNull { it.toEntity() } ?: emptyList()
            dao.insertAll(entities)
        }
    }
}
