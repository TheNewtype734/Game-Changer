package com.squires.gamechanger.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.squires.gamechanger.data.local.dao.LeagueDao
import com.squires.gamechanger.data.local.dao.TeamDao
import com.squires.gamechanger.data.local.dao.TeamDetailDao
import com.squires.gamechanger.data.local.entity.LeagueEntity
import com.squires.gamechanger.data.local.entity.TeamDetailEntity
import com.squires.gamechanger.data.local.entity.TeamEntity

@Database(
    entities = [LeagueEntity::class, TeamEntity::class, TeamDetailEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class GameChangerDatabase : RoomDatabase() {
    abstract fun leagueDao(): LeagueDao
    abstract fun teamDao(): TeamDao
    abstract fun teamDetailDao(): TeamDetailDao
}
