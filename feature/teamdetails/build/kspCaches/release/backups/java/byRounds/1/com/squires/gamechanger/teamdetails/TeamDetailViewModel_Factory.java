package com.squires.gamechanger.teamdetails;

import androidx.lifecycle.SavedStateHandle;
import com.squires.gamechanger.domain.usecase.GetTeamDetailUseCase;
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
public final class TeamDetailViewModel_Factory implements Factory<TeamDetailViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<GetTeamDetailUseCase> getTeamDetailUseCaseProvider;

  public TeamDetailViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GetTeamDetailUseCase> getTeamDetailUseCaseProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.getTeamDetailUseCaseProvider = getTeamDetailUseCaseProvider;
  }

  @Override
  public TeamDetailViewModel get() {
    return newInstance(savedStateHandleProvider.get(), getTeamDetailUseCaseProvider.get());
  }

  public static TeamDetailViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GetTeamDetailUseCase> getTeamDetailUseCaseProvider) {
    return new TeamDetailViewModel_Factory(savedStateHandleProvider, getTeamDetailUseCaseProvider);
  }

  public static TeamDetailViewModel newInstance(SavedStateHandle savedStateHandle,
      GetTeamDetailUseCase getTeamDetailUseCase) {
    return new TeamDetailViewModel(savedStateHandle, getTeamDetailUseCase);
  }
}
