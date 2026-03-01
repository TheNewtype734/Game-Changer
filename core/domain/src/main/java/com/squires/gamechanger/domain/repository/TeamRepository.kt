package com.squires.gamechanger.domain.repository

import com.squires.gamechanger.common.Result
import com.squires.gamechanger.domain.model.Team
import com.squires.gamechanger.domain.model.TeamDetail
import kotlinx.coroutines.flow.Flow

interface TeamRepository {
    fun getTeamsForLeague(leagueName: String): Flow<Result<List<Team>>>
    fun getTeamDetail(teamId: String): Flow<Result<TeamDetail>>
    suspend fun refreshTeamsForLeague(leagueName: String): Result<Unit>
}
