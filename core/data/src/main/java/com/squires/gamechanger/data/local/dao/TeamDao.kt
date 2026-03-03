package com.squires.gamechanger.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.squires.gamechanger.data.local.entity.TeamEntity

@Dao
interface TeamDao {

    @Query("SELECT * FROM teams WHERE leagueName = :leagueName ORDER BY name ASC")
    fun pagingSource(leagueName: String): PagingSource<Int, TeamEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(teams: List<TeamEntity>)
}
