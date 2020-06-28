package com.mcwilliams.theninjamethod.ui.exercises.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    var isRefreshing: Boolean = false

    val errorMessage: MutableLiveData<Int> = MutableLiveData()

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

    fun refreshData() {
        isRefreshing = true
//        loadExercises()
    }


    companion object {
        private const val TAG = "ExerciseListViewModel"
    }
}