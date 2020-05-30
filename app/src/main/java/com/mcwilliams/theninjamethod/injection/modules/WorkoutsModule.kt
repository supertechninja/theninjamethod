package com.mcwilliams.theninjamethod.injection.modules

import androidx.lifecycle.ViewModel
import com.mcwilliams.theninjamethod.injection.ViewModelFactoryModule
import com.mcwilliams.theninjamethod.injection.ViewModelKey
import com.mcwilliams.theninjamethod.network.NetworkModule
import com.mcwilliams.theninjamethod.ui.exercises.ExercisesFragment
import com.mcwilliams.theninjamethod.ui.exercises.viewmodel.ExerciseListViewModel
import com.mcwilliams.theninjamethod.ui.workouts.WorkoutListViewModel
import com.mcwilliams.theninjamethod.ui.workouts.WorkoutsFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module(includes = [ViewModelFactoryModule::class, NetworkModule::class])
abstract class WorkoutsModule {

    @ContributesAndroidInjector
    internal abstract fun contributesWorkoutsFragment(): WorkoutsFragment

    @Binds
    @IntoMap
    @ViewModelKey(WorkoutListViewModel::class)
    abstract fun bindWorkoutViewModel(workoutViewModel: WorkoutListViewModel): ViewModel
}