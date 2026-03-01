package com.squires.gamechanger.data.repository

import com.squires.gamechanger.common.Result
import com.squires.gamechanger.data.local.dao.TeamDao
import com.squires.gamechanger.data.local.dao.TeamDetailDao
import com.squires.gamechanger.data.mapper.toDomain
import com.squires.gamechanger.data.mapper.toDetailEntity
import com.squires.gamechanger.data.mapper.toEntity
import com.squires.gamechanger.data.util.toUserMessage
import com.squires.gamechanger.domain.model.Team
import com.squires.gamechanger.domain.model.TeamDetail
import com.squires.gamechanger.domain.repository.TeamRepository
import com.squires.gamechanger.network.api.SportsDbApi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class TeamRepositoryImpl @Inject constructor(
    private val api: SportsDbApi,
    private val teamDao: TeamDao,
    private val teamDetailDao: TeamDetailDao,
) : TeamRepository {

    override fun getTeamsForLeague(leagueName: String): Flow<Result<List<Team>>> = channelFlow {
        send(Result.Loading)
        when (val result = refreshTeamsForLeague(leagueName)) {
            is Result.Error -> {
                val cached = teamDao.getTeamsForLeague(leagueName).first().map { it.toDomain() }
                send(Result.Error(result.message, result.cause, cachedData = cached.ifEmpty { null }))
            }
            is Result.Success -> {
                try {
                    teamDao.getTeamsForLeague(leagueName)
                        .map<_, Result<List<Team>>> { Result.Success(it.map { e -> e.toDomain() }) }
                        .collect { send(it) }
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Throwable) {
                    val cached = teamDao.getTeamsForLeague(leagueName).first().map { it.toDomain() }
                    send(Result.Error(e.toUserMessage(), e, cachedData = cached.ifEmpty { null }))
                }
            }
            is Result.Loading -> {} // cannot occur for a suspend fun
        }
    }.flowOn(Dispatchers.IO)

    override fun getTeamDetail(teamId: String): Flow<Result<TeamDetail>> {
        return teamDetailDao.getTeamDetail(teamId)
            .map<_, Result<TeamDetail>?> { entity ->
                entity?.let { Result.Success(it.toDomain()) }
            }
            .mapNotNull { it }
            .onStart {
                emit(Result.Loading)
            }
            .catch { throwable ->
                emit(Result.Error(throwable.toUserMessage(), throwable))
            }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun refreshTeamsForLeague(leagueName: String): Result<Unit> {
        return try {
            val dtos = api.getTeamsForLeague(leagueName).teams ?: emptyList()
            teamDao.insertAll(dtos.mapNotNull { it.toEntity() })
            teamDetailDao.insertAll(dtos.mapNotNull { it.toDetailEntity() })
            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            Result.Error(e.toUserMessage(), e)
        }
    }
}
