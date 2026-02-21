package com.squires.gamechanger.data.di;

import com.squires.gamechanger.data.local.dao.TeamDetailDao;
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
public final class DatabaseModule_ProvideTeamDetailDaoFactory implements Factory<TeamDetailDao> {
  private final Provider<GameChangerDatabase> databaseProvider;

  public DatabaseModule_ProvideTeamDetailDaoFactory(
      Provider<GameChangerDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public TeamDetailDao get() {
    return provideTeamDetailDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideTeamDetailDaoFactory create(
      Provider<GameChangerDatabase> databaseProvider) {
    return new DatabaseModule_ProvideTeamDetailDaoFactory(databaseProvider);
  }

  public static TeamDetailDao provideTeamDetailDao(GameChangerDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideTeamDetailDao(database));
  }
}
