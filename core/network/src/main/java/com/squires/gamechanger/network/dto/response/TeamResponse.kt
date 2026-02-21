package com.squires.gamechanger.network.dto.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squires.gamechanger.network.dto.TeamDto

@JsonClass(generateAdapter = true)
data class TeamResponse(
    @Json(name = "teams") val teams: List<TeamDto>?,
)
