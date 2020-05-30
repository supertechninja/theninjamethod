package com.mcwilliams.theninjamethod.di.modules

import androidx.lifecycle.ViewModel
import com.mcwilliams.theninjamethod.di.ViewModelFactoryModule
import com.mcwilliams.theninjamethod.di.ViewModelKey
import com.mcwilliams.theninjamethod.ui.exercises.ExercisesFragment
import com.mcwilliams.theninjamethod.ui.exercises.viewmodel.ExerciseListViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module(includes = [ViewModelFactoryModule::class, NetworkModule::class])
abstract class ExercisesModule {

    @ContributesAndroidInjector
    internal abstract fun contributesExercisesFragment(): ExercisesFragment

    @Binds
    @IntoMap
    @ViewModelKey(ExerciseListViewModel::class)
    abstract fun bindExerciseViewModel(exerciseViewModel: ExerciseListViewModel): ViewModel
}