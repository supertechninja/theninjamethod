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
            loadExercises()
        }
    }

    val exerciseListAdapter: ExerciseListAdapter = ExerciseListAdapter()

    init {
        viewModelScope.launch {
            val data = loadExercises()
            onRetrievePostListSuccess(data)
        }

//        viewModelScope.launch {
//            val loginResult = getToken()
//            Log.d(Companion.TAG, ": " + loginResult!!.access_token)
//        }
    }

//    fun setupSwipeToDelete(){
//        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(exerciseListAdapter, this))
//        itemTouchHelper.attachToRecyclerView(recyclerView)
//    }

    private suspend fun loadExercises() = exerciseApi.getExercises()

//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .doOnSubscribe { onRetrievePostListStart() }
//            .doOnTerminate { onRetrievePostListFinish() }
//            .subscribe(
//                { result -> onRetrievePostListSuccess(result) },
//                { onRetrievePostListError() }
//            )
//    }

//    fun deleteExercise(position : Int){
//        val exerciseToDelete = exerciseListAdapter.getExerciseList()[position]
//    }

    fun addExercise(exercise: AddExerciseRequest) {
//        subscription = exerciseApi.addExercise(exercise)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(
//                { refreshData() },
//                { onRetrievePostListError() }
//            )

    }

    fun refreshData() {
        isRefreshing = true
//        loadExercises()
    }

    private fun onRetrievePostListStart() {
        loadingVisibility.value = View.VISIBLE
        errorMessage.value = null
        isRefreshing = false
    }

    private fun onRetrievePostListFinish() {
        loadingVisibility.value = View.GONE
        isRefreshing = false
    }

    private fun onRetrievePostListSuccess(exerciseList: Data) {
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