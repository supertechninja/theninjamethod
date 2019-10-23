package com.mcwilliams.theninjamethod.network;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;
import javax.inject.Provider;
import retrofit2.Retrofit;

@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class NetworkModule_ProvidePostApi$app_debugFactory implements Factory<ExerciseApi> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvidePostApi$app_debugFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public ExerciseApi get() {
    return providePostApi$app_debug(retrofitProvider.get());
  }

  public static NetworkModule_ProvidePostApi$app_debugFactory create(
      Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvidePostApi$app_debugFactory(retrofitProvider);
  }

  public static ExerciseApi providePostApi$app_debug(Retrofit retrofit) {
    return Preconditions.checkNotNull(NetworkModule.providePostApi$app_debug(retrofit), "Cannot return null from a non-@Nullable @Provides method");
  }
}
