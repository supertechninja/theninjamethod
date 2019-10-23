package com.mcwilliams.theninjamethod.injection;

import com.mcwilliams.theninjamethod.network.ExerciseApi;
import com.mcwilliams.theninjamethod.network.NetworkModule;
import com.mcwilliams.theninjamethod.network.NetworkModule_ProvidePostApi$app_debugFactory;
import com.mcwilliams.theninjamethod.network.NetworkModule_ProvideRetrofitInterface$app_debugFactory;
import com.mcwilliams.theninjamethod.viewmodel.ExerciseListViewModel;
import com.mcwilliams.theninjamethod.viewmodel.ExerciseListViewModel_MembersInjector;
import dagger.internal.Preconditions;
import dagger.internal.SingleCheck;
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
public final class DaggerViewModelInjector implements ViewModelInjector {
  private Provider<Retrofit> provideRetrofitInterface$app_debugProvider;

  private Provider<ExerciseApi> providePostApi$app_debugProvider;

  private DaggerViewModelInjector() {

    initialize();
  }

  public static ViewModelInjector.Builder builder() {
    return new Builder();
  }

  public static ViewModelInjector create() {
    return new Builder().build();
  }

  @SuppressWarnings("unchecked")
  private void initialize() {
    this.provideRetrofitInterface$app_debugProvider = SingleCheck.provider(NetworkModule_ProvideRetrofitInterface$app_debugFactory.create());
    this.providePostApi$app_debugProvider = SingleCheck.provider(NetworkModule_ProvidePostApi$app_debugFactory.create(provideRetrofitInterface$app_debugProvider));
  }

  @Override
  public void inject(ExerciseListViewModel postListViewModel) {
    injectExerciseListViewModel(postListViewModel);}

  private ExerciseListViewModel injectExerciseListViewModel(ExerciseListViewModel instance) {
    ExerciseListViewModel_MembersInjector.injectExerciseApi(instance, providePostApi$app_debugProvider.get());
    return instance;
  }

  private static final class Builder implements ViewModelInjector.Builder {
    /**
     * @deprecated This module is declared, but an instance is not used in the component. This method is a no-op. For more, see https://dagger.dev/unused-modules.
     */
    @Override
    @Deprecated
    public Builder networkModule(NetworkModule networkModule) {
      Preconditions.checkNotNull(networkModule);
      return this;
    }

    @Override
    public ViewModelInjector build() {
      return new DaggerViewModelInjector();
    }
  }
}
