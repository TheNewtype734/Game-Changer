package com.squires.gamechanger.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.squires.gamechanger.data.local.entity.TeamDetailEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDetailDao {

    @Query("SELECT * FROM team_details WHERE id = :teamId")
    fun getTeamDetail(teamId: String): Flow<TeamDetailEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(teamDetail: TeamDetailEntity)
}
