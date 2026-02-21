package com.squires.gamechanger.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.squires.gamechanger.data.local.entity.LeagueEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LeagueDao {

    @Query("SELECT * FROM leagues ORDER BY name ASC")
    fun getLeagues(): Flow<List<LeagueEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(leagues: List<LeagueEntity>)
}
