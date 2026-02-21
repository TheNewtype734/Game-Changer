package com.squires.gamechanger.network.api

import com.squires.gamechanger.network.dto.response.LeagueResponse
import com.squires.gamechanger.network.dto.response.TeamDetailResponse
import com.squires.gamechanger.network.dto.response.TeamResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SportsDbApi {

    @GET("all_leagues.php")
    suspend fun getAllLeagues(): LeagueResponse

    @GET("search_all_teams.php")
    suspend fun getTeamsForLeague(@Query("l") leagueName: String): TeamResponse

    @GET("lookupteam.php")
    suspend fun getTeamDetail(@Query("id") teamId: String): TeamDetailResponse
}
