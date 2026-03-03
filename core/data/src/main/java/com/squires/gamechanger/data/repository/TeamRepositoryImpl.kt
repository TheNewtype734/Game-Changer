package com.squires.gamechanger.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
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

    override fun getTeamsForLeaguePaged(leagueName: String): Flow<PagingData<Team>> = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { teamDao.pagingSource(leagueName) },
    ).flow
        .map { pagingData -> pagingData.map { it.toDomain() } }
        .flowOn(Dispatchers.IO)

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
