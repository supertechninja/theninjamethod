package com.mcwilliams.theninjamethod.viewmodel;

import com.mcwilliams.theninjamethod.network.ExerciseApi;
import dagger.MembersInjector;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class ExerciseListViewModel_MembersInjector implements MembersInjector<ExerciseListViewModel> {
  private final Provider<ExerciseApi> exerciseApiProvider;

  public ExerciseListViewModel_MembersInjector(Provider<ExerciseApi> exerciseApiProvider) {
    this.exerciseApiProvider = exerciseApiProvider;
  }

  public static MembersInjector<ExerciseListViewModel> create(
      Provider<ExerciseApi> exerciseApiProvider) {
    return new ExerciseListViewModel_MembersInjector(exerciseApiProvider);}

  @Override
  public void injectMembers(ExerciseListViewModel instance) {
    injectExerciseApi(instance, exerciseApiProvider.get());
  }

  public static void injectExerciseApi(ExerciseListViewModel instance, ExerciseApi exerciseApi) {
    instance.exerciseApi = exerciseApi;
  }
}
