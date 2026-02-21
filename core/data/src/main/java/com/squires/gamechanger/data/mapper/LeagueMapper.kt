package com.squires.gamechanger.data.mapper

import com.squires.gamechanger.data.local.entity.LeagueEntity
import com.squires.gamechanger.domain.model.League
import com.squires.gamechanger.network.dto.LeagueDto

fun LeagueDto.toEntity(): LeagueEntity? {
    val id = id ?: return null
    val name = name ?: return null
    return LeagueEntity(
        id = id,
        name = name,
        sport = sport ?: "",
        badgeUrl = badgeUrl,
        country = country,
    )
}

fun LeagueEntity.toDomain(): League = League(
    id = id,
    name = name,
    sport = sport,
    badgeUrl = badgeUrl,
    country = country,
)
