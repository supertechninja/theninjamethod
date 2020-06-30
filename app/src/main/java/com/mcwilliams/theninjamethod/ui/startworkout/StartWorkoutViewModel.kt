package com.mcwilliams.theninjamethod.ui.startworkout

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcwilliams.theninjamethod.ui.exercises.db.Exercise
import com.mcwilliams.theninjamethod.ui.exercises.repository.ExerciseRepository
import com.mcwilliams.theninjamethod.ui.ext.toLiveData
import com.mcwilliams.theninjamethod.ui.workouts.manualworkoutdetail.ManualWorkoutsRepository
import com.mcwilliams.theninjamethod.ui.workouts.manualworkoutdetail.db.Workout
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch

class StartWorkoutViewModel @ViewModelInject constructor(
    private val manualWorkoutsRepository: ManualWorkoutsRepository,
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {

    var listOfExercises: LiveData<List<Exercise>>

    private var _didSaveWorkout = MutableLiveData<Boolean>()
    var didSaveWorkout: LiveData<Boolean> = _didSaveWorkout

    var compositeDisposable = CompositeDisposable()

    init {
        listOfExercises = exerciseRepository.getExercises()!!.toObservable()
            .toLiveData(compositeDisposable) { it }
    }

    fun saveWorkout(workout: Workout) {
        viewModelScope.launch {
            manualWorkoutsRepository.addWorkout(workout)
            _didSaveWorkout.postValue(true)
        }
    }

    fun addNewExercise(exercise: Exercise) {
        viewModelScope.launch {
            exerciseRepository.addExercises(exercise)
        }
    }

}