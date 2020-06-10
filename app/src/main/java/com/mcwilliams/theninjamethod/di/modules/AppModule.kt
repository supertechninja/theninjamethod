package com.mcwilliams.theninjamethod.di.modules

import androidx.lifecycle.ViewModel
import com.mcwilliams.theninjamethod.di.ViewModelFactoryModule
import com.mcwilliams.theninjamethod.di.ViewModelKey
import com.mcwilliams.theninjamethod.ui.settings.repo.AthleteRepo
import com.mcwilliams.theninjamethod.ui.settings.repo.AthleteRepoSharedPrefImpl
import com.mcwilliams.theninjamethod.ui.workouts.WorkoutListViewModel
import com.mcwilliams.theninjamethod.ui.workouts.WorkoutsFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module(includes = [ViewModelFactoryModule::class, NetworkModule::class])
abstract class AppModule {

//    @ContributesAndroidInjector
//    internal abstract fun contributesWorkoutsFragment(): WorkoutsFragment

//    @Binds
//    @IntoMap
//    @ViewModelKey(WorkoutListViewModel::class)
//    abstract fun bindWorkoutViewModel(workoutViewModel: WorkoutListViewModel): ViewModel

    @Binds
    abstract fun bindAhtleteRepo(a: AthleteRepoSharedPrefImpl): AthleteRepo
}