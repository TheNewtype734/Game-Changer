package com.squires.gamechanger.network.di;

import com.squires.gamechanger.network.api.SportsDbApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import retrofit2.Retrofit;

@ScopeMetadata("javax.inject.Singleton")
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
public final class NetworkModule_ProvideSportsDbApiFactory implements Factory<SportsDbApi> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvideSportsDbApiFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public SportsDbApi get() {
    return provideSportsDbApi(retrofitProvider.get());
  }

  public static NetworkModule_ProvideSportsDbApiFactory create(
      Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvideSportsDbApiFactory(retrofitProvider);
  }

  public static SportsDbApi provideSportsDbApi(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideSportsDbApi(retrofit));
  }
}
