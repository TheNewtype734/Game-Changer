package com.squires.gamechanger.network.dto.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squires.gamechanger.network.dto.LeagueDto

@JsonClass(generateAdapter = true)
data class LeagueResponse(
    @Json(name = "leagues") val leagues: List<LeagueDto>?,
)
