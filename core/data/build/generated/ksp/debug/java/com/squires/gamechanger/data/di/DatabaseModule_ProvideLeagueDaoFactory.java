package com.squires.gamechanger.data.di;

import com.squires.gamechanger.data.local.dao.LeagueDao;
import com.squires.gamechanger.data.local.db.GameChangerDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideLeagueDaoFactory implements Factory<LeagueDao> {
  private final Provider<GameChangerDatabase> databaseProvider;

  public DatabaseModule_ProvideLeagueDaoFactory(Provider<GameChangerDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public LeagueDao get() {
    return provideLeagueDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideLeagueDaoFactory create(
      Provider<GameChangerDatabase> databaseProvider) {
    return new DatabaseModule_ProvideLeagueDaoFactory(databaseProvider);
  }

  public static LeagueDao provideLeagueDao(GameChangerDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideLeagueDao(database));
  }
}
