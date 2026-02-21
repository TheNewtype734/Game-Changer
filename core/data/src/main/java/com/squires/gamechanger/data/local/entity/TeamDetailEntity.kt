package com.squires.gamechanger.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "team_details")
data class TeamDetailEntity(
    @PrimaryKey val id: String,
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
