package com.squires.gamechanger.teams;

import androidx.lifecycle.SavedStateHandle;
import com.squires.gamechanger.domain.usecase.GetTeamsForLeagueUseCase;
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
public final class TeamsViewModel_Factory implements Factory<TeamsViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<GetTeamsForLeagueUseCase> getTeamsForLeagueUseCaseProvider;

  public TeamsViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GetTeamsForLeagueUseCase> getTeamsForLeagueUseCaseProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.getTeamsForLeagueUseCaseProvider = getTeamsForLeagueUseCaseProvider;
  }

  @Override
  public TeamsViewModel get() {
    return newInstance(savedStateHandleProvider.get(), getTeamsForLeagueUseCaseProvider.get());
  }

  public static TeamsViewModel_Factory create(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GetTeamsForLeagueUseCase> getTeamsForLeagueUseCaseProvider) {
    return new TeamsViewModel_Factory(savedStateHandleProvider, getTeamsForLeagueUseCaseProvider);
  }

  public static TeamsViewModel newInstance(SavedStateHandle savedStateHandle,
      GetTeamsForLeagueUseCase getTeamsForLeagueUseCase) {
    return new TeamsViewModel(savedStateHandle, getTeamsForLeagueUseCase);
  }
}
