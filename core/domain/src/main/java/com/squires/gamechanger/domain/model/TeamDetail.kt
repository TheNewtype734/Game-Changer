package com.squires.gamechanger.domain.model

data class TeamDetail(
    val id: String,
    val name: String,
    val leagueName: String,
    val sport: String?,
    val country: String?,
    val formedYear: String?,
    val description: String?,
    val stadium: String?,
    val stadiumLocation: String?,
    val stadiumCapacity: String?,
    val badgeUrl: String?,
    val bannerUrl: String?,
    val website: String?,
)
