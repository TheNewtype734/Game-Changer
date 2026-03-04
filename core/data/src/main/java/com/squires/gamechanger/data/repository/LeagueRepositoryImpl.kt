package com.squires.gamechanger.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
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
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LeagueRepositoryImpl @Inject constructor(
    private val api: SportsDbApi,
    private val dao: LeagueDao,
) : LeagueRepository {

    private val pagingConfig = PagingConfig(pageSize = 20, enablePlaceholders = false)

    override fun getLeaguesPaged(): Flow<PagingData<League>> = Pager(
        config = pagingConfig,
        pagingSourceFactory = { dao.pagingSource() },
    ).flow
        .map { pagingData -> pagingData.map { it.toDomain() } }
        .flowOn(Dispatchers.IO)

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

    override suspend fun hasLeagues(): Boolean =
        withContext(Dispatchers.IO) { dao.count() > 0 }

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
