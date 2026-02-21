package com.squires.gamechanger.data.repository

import com.squires.gamechanger.common.Result
import com.squires.gamechanger.data.local.dao.TeamDao
import com.squires.gamechanger.data.local.dao.TeamDetailDao
import com.squires.gamechanger.data.mapper.toDomain
import com.squires.gamechanger.data.mapper.toEntity
import com.squires.gamechanger.domain.model.Team
import com.squires.gamechanger.domain.model.TeamDetail
import com.squires.gamechanger.domain.repository.TeamRepository
import com.squires.gamechanger.network.api.SportsDbApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class TeamRepositoryImpl @Inject constructor(
    private val api: SportsDbApi,
    private val teamDao: TeamDao,
    private val teamDetailDao: TeamDetailDao,
) : TeamRepository {

    override fun getTeamsForLeague(leagueName: String): Flow<Result<List<Team>>> {
        return teamDao.getTeamsForLeague(leagueName)
            .map<_, Result<List<Team>>> { entities ->
                Result.Success(entities.map { it.toDomain() })
            }
            .onStart {
                emit(Result.Loading)
                refreshTeams(leagueName)
            }
            .catch { throwable ->
                emit(Result.Error(throwable.message ?: "Unknown error", throwable))
            }
    }

    override fun getTeamDetail(teamId: String): Flow<Result<TeamDetail>> {
        return teamDetailDao.getTeamDetail(teamId)
            .mapNotNull { it }
            .map<_, Result<TeamDetail>> { entity ->
                Result.Success(entity.toDomain())
            }
            .onStart {
                emit(Result.Loading)
                refreshTeamDetail(teamId)
            }
            .catch { throwable ->
                emit(Result.Error(throwable.message ?: "Unknown error", throwable))
            }
    }

    private suspend fun refreshTeams(leagueName: String) {
        runCatching {
            val response = api.getTeamsForLeague(leagueName)
            val entities = response.teams?.mapNotNull { it.toEntity() } ?: emptyList()
            teamDao.insertAll(entities)
        }
    }

    private suspend fun refreshTeamDetail(teamId: String) {
        runCatching {
            val response = api.getTeamDetail(teamId)
            val entity = response.teams?.firstOrNull()?.toEntity()
            if (entity != null) {
                teamDetailDao.insert(entity)
            }
        }
    }
}
