package com.squires.gamechanger.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "leagues")
data class LeagueEntity(
    @PrimaryKey val id: String,
    val name: String,
    val sport: String,
    val badgeUrl: String?,
    val country: String?,
)
