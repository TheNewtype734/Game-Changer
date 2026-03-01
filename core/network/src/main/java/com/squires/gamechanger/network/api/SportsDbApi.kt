package com.squires.gamechanger.network.api

import com.squires.gamechanger.network.dto.response.LeagueResponse
import com.squires.gamechanger.network.dto.response.LeagueSearchResponse
import com.squires.gamechanger.network.dto.response.TeamResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SportsDbApi {

    @GET("all_leagues.php")
    suspend fun getAllLeagues(): LeagueResponse

    @GET("search_all_leagues.php")
    suspend fun searchLeagues(
        @Query("c") country: String? = null,
        @Query("s") sport: String? = null,
    ): LeagueSearchResponse

    @GET("search_all_teams.php")
    suspend fun getTeamsForLeague(@Query("l") leagueName: String): TeamResponse
}
