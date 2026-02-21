package com.squires.gamechanger.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TeamDetailDto(
    @Json(name = "idTeam") val id: String?,
    @Json(name = "strTeam") val name: String?,
    @Json(name = "strLeague") val leagueName: String?,
    @Json(name = "strSport") val sport: String?,
    @Json(name = "strCountry") val country: String?,
    @Json(name = "intFormedYear") val formedYear: String?,
    @Json(name = "strDescriptionEN") val description: String?,
    @Json(name = "strStadium") val stadium: String?,
    @Json(name = "strStadiumLocation") val stadiumLocation: String?,
    @Json(name = "intStadiumCapacity") val stadiumCapacity: String?,
    @Json(name = "strTeamBadge") val badgeUrl: String?,
    @Json(name = "strTeamBanner") val bannerUrl: String?,
    @Json(name = "strWebsite") val website: String?,
)
