package com.mcwilliams.theninjamethod.ui.exercises.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.model.*
import com.mcwilliams.theninjamethod.network.ExerciseApi
import com.mcwilliams.theninjamethod.ui.exercises.ExerciseListAdapter
import kotlinx.coroutines.launch
import javax.inject.Inject

class ExerciseListViewModel @Inject constructor(
    private val exerciseApi: ExerciseApi
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

    private fun loadExercises(){
        viewModelScope.launch {
            val data = exerciseApi.getExercises()
            onRetrievePostListSuccess(data)
        }
    }

    fun addExercise(exercise: AddExerciseRequest) {
        viewModelScope.launch {
            exerciseApi.addExercise(exercise)
            refreshData()
        }
    }

    fun refreshData() {
        isRefreshing = true
        loadExercises()
    }

    private fun onRetrievePostListSuccess(exerciseList: Data) {
        loadingVisibility.value = View.GONE
        exerciseListAdapter.updatePostList(exerciseList.exercises)
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