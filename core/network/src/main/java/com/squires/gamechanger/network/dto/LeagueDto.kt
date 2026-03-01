package com.squires.gamechanger.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LeagueDto(
    @Json(name = "idLeague") val id: String?,
    @Json(name = "strLeague") val name: String?,
    @Json(name = "strSport") val sport: String?,
    @Json(name = "strBadge") val badgeUrl: String?,
    @Json(name = "strCountry") val country: String?,
)
