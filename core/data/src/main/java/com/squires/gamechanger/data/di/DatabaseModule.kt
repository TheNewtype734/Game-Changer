package com.squires.gamechanger.data.di

import android.content.Context
import androidx.room.Room
import com.squires.gamechanger.data.local.dao.LeagueDao
import com.squires.gamechanger.data.local.dao.TeamDao
import com.squires.gamechanger.data.local.dao.TeamDetailDao
import com.squires.gamechanger.data.local.db.GameChangerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): GameChangerDatabase {
        return Room.databaseBuilder(
            context,
            GameChangerDatabase::class.java,
            "gamechanger.db",
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideLeagueDao(database: GameChangerDatabase): LeagueDao = database.leagueDao()

    @Provides
    @Singleton
    fun provideTeamDao(database: GameChangerDatabase): TeamDao = database.teamDao()

    @Provides
    @Singleton
    fun provideTeamDetailDao(database: GameChangerDatabase): TeamDetailDao = database.teamDetailDao()
}
