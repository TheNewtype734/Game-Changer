package com.squires.gamechanger.leagues;

import com.squires.gamechanger.domain.usecase.GetLeaguesUseCase;
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
public final class LeaguesViewModel_Factory implements Factory<LeaguesViewModel> {
  private final Provider<GetLeaguesUseCase> getLeaguesUseCaseProvider;

  public LeaguesViewModel_Factory(Provider<GetLeaguesUseCase> getLeaguesUseCaseProvider) {
    this.getLeaguesUseCaseProvider = getLeaguesUseCaseProvider;
  }

  @Override
  public LeaguesViewModel get() {
    return newInstance(getLeaguesUseCaseProvider.get());
  }

  public static LeaguesViewModel_Factory create(
      Provider<GetLeaguesUseCase> getLeaguesUseCaseProvider) {
    return new LeaguesViewModel_Factory(getLeaguesUseCaseProvider);
  }

  public static LeaguesViewModel newInstance(GetLeaguesUseCase getLeaguesUseCase) {
    return new LeaguesViewModel(getLeaguesUseCase);
  }
}
