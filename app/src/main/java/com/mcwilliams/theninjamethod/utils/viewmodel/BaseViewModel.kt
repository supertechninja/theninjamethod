package com.mcwilliams.theninjamethod.utils.viewmodel

import androidx.lifecycle.ViewModel
import com.mcwilliams.theninjamethod.injection.DaggerViewModelInjector
import com.mcwilliams.theninjamethod.injection.ViewModelInjector
import com.mcwilliams.theninjamethod.network.NetworkModule
import com.mcwilliams.theninjamethod.ui.workouts.WorkoutListViewModel
import com.mcwilliams.theninjamethod.ui.exercises.viewmodel.ExerciseListViewModel

abstract class BaseViewModel: ViewModel(){
    //...
    private val injector: ViewModelInjector = DaggerViewModelInjector
        .builder()
        .networkModule(NetworkModule)
        .build()

    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is ExerciseListViewModel -> injector.inject(this)
            is WorkoutListViewModel -> injector.inject(this)
        }
    }

}