package com.squires.gamechanger.network.dto.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squires.gamechanger.network.dto.LeagueDto

@JsonClass(generateAdapter = true)
data class LeagueSearchResponse(
    // TheSportsDB's search_all_leagues.php returns league results under the "countries" key.
    @Json(name = "countries") val leagues: List<LeagueDto>?,
)
