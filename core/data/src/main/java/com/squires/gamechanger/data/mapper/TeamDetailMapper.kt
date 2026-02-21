package com.squires.gamechanger.data.mapper

import com.squires.gamechanger.data.local.entity.TeamDetailEntity
import com.squires.gamechanger.domain.model.TeamDetail
import com.squires.gamechanger.network.dto.TeamDetailDto

fun TeamDetailDto.toEntity(): TeamDetailEntity? {
    val id = id ?: return null
    val name = name ?: return null
    val league = leagueName ?: return null
    return TeamDetailEntity(
        id = id,
        name = name,
        leagueName = league,
        sport = sport,
        country = country,
        formedYear = formedYear,
        description = description,
        stadium = stadium,
        stadiumLocation = stadiumLocation,
        stadiumCapacity = stadiumCapacity,
        badgeUrl = badgeUrl,
        bannerUrl = bannerUrl,
        website = website,
    )
}

fun TeamDetailEntity.toDomain(): TeamDetail = TeamDetail(
    id = id,
    name = name,
    leagueName = leagueName,
    sport = sport,
    country = country,
    formedYear = formedYear,
    description = description,
    stadium = stadium,
    stadiumLocation = stadiumLocation,
    stadiumCapacity = stadiumCapacity,
    badgeUrl = badgeUrl,
    bannerUrl = bannerUrl,
    website = website,
)
