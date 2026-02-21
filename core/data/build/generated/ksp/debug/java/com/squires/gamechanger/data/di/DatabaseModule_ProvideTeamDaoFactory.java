package com.squires.gamechanger.data.di;

import com.squires.gamechanger.data.local.dao.TeamDao;
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
public final class DatabaseModule_ProvideTeamDaoFactory implements Factory<TeamDao> {
  private final Provider<GameChangerDatabase> databaseProvider;

  public DatabaseModule_ProvideTeamDaoFactory(Provider<GameChangerDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public TeamDao get() {
    return provideTeamDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideTeamDaoFactory create(
      Provider<GameChangerDatabase> databaseProvider) {
    return new DatabaseModule_ProvideTeamDaoFactory(databaseProvider);
  }

  public static TeamDao provideTeamDao(GameChangerDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideTeamDao(database));
  }
}
