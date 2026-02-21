package com.squires.gamechanger.data.di

import com.squires.gamechanger.data.repository.LeagueRepositoryImpl
import com.squires.gamechanger.data.repository.TeamRepositoryImpl
import com.squires.gamechanger.domain.repository.LeagueRepository
import com.squires.gamechanger.domain.repository.TeamRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindLeagueRepository(impl: LeagueRepositoryImpl): LeagueRepository

    @Binds
    @Singleton
    abstract fun bindTeamRepository(impl: TeamRepositoryImpl): TeamRepository
}
