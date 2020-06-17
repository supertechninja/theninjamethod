package com.mcwilliams.theninjamethod.ui.exercises.viewmodel

import android.view.View
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.network.apis.ExerciseApi
import com.mcwilliams.theninjamethod.ui.exercises.ExerciseListAdapter
import com.mcwilliams.theninjamethod.ui.exercises.db.Exercise
import com.mcwilliams.theninjamethod.ui.exercises.repository.ExerciseRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ExerciseListViewModel @ViewModelInject constructor(
    private val exerciseApi: ExerciseApi,
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    var isRefreshing: Boolean = false

    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener {
        viewModelScope.launch {
            exerciseApi.getExercises()
        }
    }

    val exerciseListAdapter: ExerciseListAdapter = ExerciseListAdapter()

    init {
        loadExercises()
    }

    private fun loadExercises() {
        viewModelScope.launch {
            val data = exerciseApi.getExercises()
            data.exercises.forEach {
                runBlocking { exerciseRepository.addExercises(it) }
            }

            onRetrievePostListSuccess(data.exercises)
        }
    }

    fun refreshData() {
        isRefreshing = true
        loadExercises()
    }

    private fun onRetrievePostListSuccess(exerciseList: List<Exercise>) {
        loadingVisibility.value = View.GONE
        exerciseListAdapter.updatePostList(exerciseList)
    }

    private fun onRetrievePostListError() {
        errorMessage.value = R.string.exercise_error
    }

    override fun onCleared() {
        super.onCleared()
//        subscription.dispose()
    }

    companion object {
        private const val TAG = "ExerciseListViewModel"
    }
}