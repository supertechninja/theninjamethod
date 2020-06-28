package com.mcwilliams.theninjamethod.ui.exercises.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcwilliams.theninjamethod.ui.exercises.db.Exercise
import com.mcwilliams.theninjamethod.ui.exercises.repository.ExerciseRepository
import com.mcwilliams.theninjamethod.ui.ext.toLiveData
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch

class ExerciseListViewModel @ViewModelInject constructor(
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {

    var exerciseList: LiveData<List<Exercise>>
    var compositeDisposable = CompositeDisposable()

    init {
        exerciseList = exerciseRepository.getExercises()!!.toObservable()
            .toLiveData(compositeDisposable) { it }
    }

    fun addNewExercise(exercise: Exercise) {
        viewModelScope.launch {
            exerciseRepository.addExercises(exercise)
        }
    }

    fun deleteExercise(exercise: Exercise) {
        viewModelScope.launch { exerciseRepository.deleteExercise(exercise) }
    }

}