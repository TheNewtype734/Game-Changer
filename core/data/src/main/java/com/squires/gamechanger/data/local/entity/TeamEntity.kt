package com.squires.gamechanger.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "teams")
data class TeamEntity(
    @PrimaryKey val id: String,
    val name: String,
    val leagueName: String,
    val badgeUrl: String?,
    val sport: String?,
)
