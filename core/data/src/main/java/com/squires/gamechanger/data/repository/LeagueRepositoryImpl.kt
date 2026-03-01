package com.squires.gamechanger.data.repository

import com.squires.gamechanger.common.Result
import com.squires.gamechanger.data.local.dao.LeagueDao
import com.squires.gamechanger.data.mapper.toDomain
import com.squires.gamechanger.data.mapper.toEntity
import com.squires.gamechanger.data.util.toUserMessage
import com.squires.gamechanger.domain.model.League
import com.squires.gamechanger.domain.repository.LeagueRepository
import com.squires.gamechanger.network.api.SportsDbApi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LeagueRepositoryImpl @Inject constructor(
    private val api: SportsDbApi,
    private val dao: LeagueDao,
) : LeagueRepository {

    override fun getLeagues(): Flow<Result<List<League>>> = channelFlow {
        send(Result.Loading)
        when (val result = refreshLeagues()) {
            is Result.Error -> {
                val cached = dao.getLeagues().first().map { it.toDomain() }
                send(Result.Error(result.message, result.cause, cachedData = cached.ifEmpty { null }))
            }
            is Result.Success -> {
                try {
                    dao.getLeagues()
                        .map<_, Result<List<League>>> { Result.Success(it.map { e -> e.toDomain() }) }
                        .collect { send(it) }
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Throwable) {
                    val cached = dao.getLeagues().first().map { it.toDomain() }
                    send(Result.Error(e.toUserMessage(), e, cachedData = cached.ifEmpty { null }))
                }
            }
            is Result.Loading -> {} // cannot occur for a suspend fun
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun searchLeagues(query: String): Result<List<League>> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.searchLeagues(country = query)
                val leagues = response.leagues?.mapNotNull { it.toEntity()?.toDomain() } ?: emptyList()
                Result.Success(leagues)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                Result.Error(e.toUserMessage(), e)
            }
        }

    override suspend fun refreshLeagues(): Result<Unit> {
        return try {
            val response = api.getAllLeagues()
            val entities = response.leagues?.mapNotNull { it.toEntity() } ?: emptyList()
            dao.insertAll(entities)
            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            Result.Error(e.toUserMessage(), e)
        }
    }
}
