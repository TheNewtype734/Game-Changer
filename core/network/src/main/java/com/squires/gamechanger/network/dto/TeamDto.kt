package com.squires.gamechanger.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TeamDto(
    @Json(name = "idTeam") val id: String?,
    @Json(name = "strTeam") val name: String?,
    @Json(name = "strLeague") val leagueName: String?,
    @Json(name = "strTeamBadge") val badgeUrl: String?,
    @Json(name = "strSport") val sport: String?,
)
