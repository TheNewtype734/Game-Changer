package com.squires.gamechanger.data.repository;

import com.squires.gamechanger.data.local.dao.LeagueDao;
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
public final class LeagueRepositoryImpl_Factory implements Factory<LeagueRepositoryImpl> {
  private final Provider<SportsDbApi> apiProvider;

  private final Provider<LeagueDao> daoProvider;

  public LeagueRepositoryImpl_Factory(Provider<SportsDbApi> apiProvider,
      Provider<LeagueDao> daoProvider) {
    this.apiProvider = apiProvider;
    this.daoProvider = daoProvider;
  }

  @Override
  public LeagueRepositoryImpl get() {
    return newInstance(apiProvider.get(), daoProvider.get());
  }

  public static LeagueRepositoryImpl_Factory create(Provider<SportsDbApi> apiProvider,
      Provider<LeagueDao> daoProvider) {
    return new LeagueRepositoryImpl_Factory(apiProvider, daoProvider);
  }

  public static LeagueRepositoryImpl newInstance(SportsDbApi api, LeagueDao dao) {
    return new LeagueRepositoryImpl(api, dao);
  }
}
