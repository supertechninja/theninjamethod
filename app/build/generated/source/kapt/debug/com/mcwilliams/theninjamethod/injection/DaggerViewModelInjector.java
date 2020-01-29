package com.mcwilliams.theninjamethod.injection;

import com.mcwilliams.theninjamethod.network.ExerciseApi;
import com.mcwilliams.theninjamethod.network.NetworkModule;
import com.mcwilliams.theninjamethod.network.NetworkModule_ProvideExerciseApi$app_debugFactory;
import com.mcwilliams.theninjamethod.network.NetworkModule_ProvideRetrofitInterface$app_debugFactory;
import com.mcwilliams.theninjamethod.network.NetworkModule_ProvideWorkoutApi$app_debugFactory;
import com.mcwilliams.theninjamethod.network.WorkoutApi;
import com.mcwilliams.theninjamethod.ui.exercises.viewmodel.ExerciseListViewModel;
import com.mcwilliams.theninjamethod.ui.exercises.viewmodel.ExerciseListViewModel_MembersInjector;
import com.mcwilliams.theninjamethod.ui.workouts.WorkoutListViewModel;
import com.mcwilliams.theninjamethod.ui.workouts.WorkoutListViewModel_MembersInjector;
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

  private Provider<ExerciseApi> provideExerciseApi$app_debugProvider;

  private Provider<WorkoutApi> provideWorkoutApi$app_debugProvider;

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
    this.provideExerciseApi$app_debugProvider = SingleCheck.provider(NetworkModule_ProvideExerciseApi$app_debugFactory.create(provideRetrofitInterface$app_debugProvider));
    this.provideWorkoutApi$app_debugProvider = SingleCheck.provider(NetworkModule_ProvideWorkoutApi$app_debugFactory.create(provideRetrofitInterface$app_debugProvider));
  }

  @Override
  public void inject(ExerciseListViewModel exerciseListViewModel) {
    injectExerciseListViewModel(exerciseListViewModel);}

  @Override
  public void inject(WorkoutListViewModel workoutListViewModel) {
    injectWorkoutListViewModel(workoutListViewModel);}

  private ExerciseListViewModel injectExerciseListViewModel(ExerciseListViewModel instance) {
    ExerciseListViewModel_MembersInjector.injectExerciseApi(instance, provideExerciseApi$app_debugProvider.get());
    return instance;
  }

  private WorkoutListViewModel injectWorkoutListViewModel(WorkoutListViewModel instance) {
    WorkoutListViewModel_MembersInjector.injectWorkoutApi(instance, provideWorkoutApi$app_debugProvider.get());
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
