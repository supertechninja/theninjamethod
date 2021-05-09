package com.mcwilliams.theninjamethod.ui.exercises.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcwilliams.data.exercisedb.DbExercise
import com.mcwilliams.theninjamethod.ui.exercises.repository.ExerciseRepository
import com.mcwilliams.theninjamethod.ui.ext.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseListViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {

    var exerciseList: LiveData<List<DbExercise>>

    val _showDialog = MutableLiveData(false)
    val showDialog = _showDialog

    val _showChipFilters = MutableLiveData(false)
    val showChipFilters = _showChipFilters

    var compositeDisposable = CompositeDisposable()

    init {
        exerciseList = exerciseRepository.getExercises()!!.toObservable()
            .toLiveData(compositeDisposable) { it }
    }

    fun addNewExercise(exercise: DbExercise) {
        viewModelScope.launch {
            exerciseRepository.addExercises(exercise)
        }
    }

    fun deleteExercise(exercise: DbExercise) {
        viewModelScope.launch { exerciseRepository.deleteExercise(exercise) }
    }

    fun nukeExercisesTable() {
        viewModelScope.launch { exerciseRepository.nukeTable() }
    }

}