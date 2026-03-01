package com.squires.gamechanger.leagues;

import com.squires.gamechanger.domain.usecase.GetLeaguesUseCase;
import com.squires.gamechanger.domain.usecase.SearchLeaguesUseCase;
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

  private final Provider<SearchLeaguesUseCase> searchLeaguesUseCaseProvider;

  public LeaguesViewModel_Factory(Provider<GetLeaguesUseCase> getLeaguesUseCaseProvider,
      Provider<SearchLeaguesUseCase> searchLeaguesUseCaseProvider) {
    this.getLeaguesUseCaseProvider = getLeaguesUseCaseProvider;
    this.searchLeaguesUseCaseProvider = searchLeaguesUseCaseProvider;
  }

  @Override
  public LeaguesViewModel get() {
    return newInstance(getLeaguesUseCaseProvider.get(), searchLeaguesUseCaseProvider.get());
  }

  public static LeaguesViewModel_Factory create(
      Provider<GetLeaguesUseCase> getLeaguesUseCaseProvider,
      Provider<SearchLeaguesUseCase> searchLeaguesUseCaseProvider) {
    return new LeaguesViewModel_Factory(getLeaguesUseCaseProvider, searchLeaguesUseCaseProvider);
  }

  public static LeaguesViewModel newInstance(GetLeaguesUseCase getLeaguesUseCase,
      SearchLeaguesUseCase searchLeaguesUseCase) {
    return new LeaguesViewModel(getLeaguesUseCase, searchLeaguesUseCase);
  }
}
