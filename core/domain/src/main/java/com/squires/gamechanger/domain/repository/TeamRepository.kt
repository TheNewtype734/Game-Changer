package com.squires.gamechanger.domain.repository

import androidx.paging.PagingData
import com.squires.gamechanger.common.Result
import com.squires.gamechanger.domain.model.Team
import com.squires.gamechanger.domain.model.TeamDetail
import kotlinx.coroutines.flow.Flow

interface TeamRepository {
    fun getTeamsForLeaguePaged(leagueName: String): Flow<PagingData<Team>>
    suspend fun hasTeamsForLeague(leagueName: String): Boolean
    fun getTeamDetail(teamId: String): Flow<Result<TeamDetail>>
    suspend fun refreshTeamsForLeague(leagueName: String): Result<Unit>
}
