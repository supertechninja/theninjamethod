package com.mcwilliams.theninjamethod.injection

import com.mcwilliams.theninjamethod.network.NetworkModule
import com.mcwilliams.theninjamethod.ui.workouts.WorkoutListViewModel
import com.mcwilliams.theninjamethod.ui.exercises.viewmodel.ExerciseListViewModel
import dagger.Component
import javax.inject.Singleton

/**
 * Component providing inject() methods for presenters.
 */
@Singleton
@Component(modules = [(NetworkModule::class)])
interface ViewModelInjector {
    /**
     * Injects required dependencies into the specified PostListViewModel.
     * @param exerciseListViewModel PostListViewModel in which to inject the dependencies
     */
    fun inject(exerciseListViewModel: ExerciseListViewModel)

    fun inject(workoutListViewModel: WorkoutListViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector

        fun networkModule(networkModule: NetworkModule): Builder
    }
}