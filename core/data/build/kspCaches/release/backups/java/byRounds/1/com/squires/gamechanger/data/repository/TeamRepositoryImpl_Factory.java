package com.squires.gamechanger.data.repository;

import com.squires.gamechanger.data.local.dao.TeamDao;
import com.squires.gamechanger.data.local.dao.TeamDetailDao;
import com.squires.gamechanger.network.api.SportsDbApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class TeamRepositoryImpl_Factory implements Factory<TeamRepositoryImpl> {
  private final Provider<SportsDbApi> apiProvider;

  private final Provider<TeamDao> teamDaoProvider;

  private final Provider<TeamDetailDao> teamDetailDaoProvider;

  public TeamRepositoryImpl_Factory(Provider<SportsDbApi> apiProvider,
      Provider<TeamDao> teamDaoProvider, Provider<TeamDetailDao> teamDetailDaoProvider) {
    this.apiProvider = apiProvider;
    this.teamDaoProvider = teamDaoProvider;
    this.teamDetailDaoProvider = teamDetailDaoProvider;
  }

  @Override
  public TeamRepositoryImpl get() {
    return newInstance(apiProvider.get(), teamDaoProvider.get(), teamDetailDaoProvider.get());
  }

  public static TeamRepositoryImpl_Factory create(Provider<SportsDbApi> apiProvider,
      Provider<TeamDao> teamDaoProvider, Provider<TeamDetailDao> teamDetailDaoProvider) {
    return new TeamRepositoryImpl_Factory(apiProvider, teamDaoProvider, teamDetailDaoProvider);
  }

  public static TeamRepositoryImpl newInstance(SportsDbApi api, TeamDao teamDao,
      TeamDetailDao teamDetailDao) {
    return new TeamRepositoryImpl(api, teamDao, teamDetailDao);
  }
}
