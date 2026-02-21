package com.squires.gamechanger.network.dto.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squires.gamechanger.network.dto.TeamDetailDto

@JsonClass(generateAdapter = true)
data class TeamDetailResponse(
    @Json(name = "teams") val teams: List<TeamDetailDto>?,
)
