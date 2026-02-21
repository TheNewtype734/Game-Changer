package com.squires.gamechanger.domain.model

data class League(
    val id: String,
    val name: String,
    val sport: String,
    val badgeUrl: String?,
    val country: String?,
)
