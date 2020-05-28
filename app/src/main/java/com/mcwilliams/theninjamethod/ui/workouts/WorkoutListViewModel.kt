package com.mcwilliams.theninjamethod.ui.workouts

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mcwilliams.theninjamethod.model.*
import com.mcwilliams.theninjamethod.network.WorkoutApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class WorkoutListViewModel @Inject constructor(
    private val workoutApi: WorkoutApi
) : ViewModel() {

    private val TAG = "WorkoutListViewModel"
    private lateinit var subscription: Disposable

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    var isRefreshing: Boolean = false
    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadWorkouts() }

    val workoutListAdapter: WorkoutListAdapter = WorkoutListAdapter()

    init {
        loadWorkouts()
    }

//    fun setupSwipeToDelete(){
//        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(exerciseListAdapter, this))
//        itemTouchHelper.attachToRecyclerView(recyclerView)
//    }

    private fun loadWorkouts() {
        subscription = workoutApi.getWorkouts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRetrievePostListStart() }
            .doOnTerminate { onRetrievePostListFinish() }
            .subscribe(
                { result -> onRetrievePostListSuccess(result) },
                { onRetrievePostListError() }
            )
    }

//    fun deleteExercise(position : Int){
//        val exerciseToDelete = exerciseListAdapter.getExerciseList()[position]
//    }

    //    fun addExercise(exercise: AddExerciseRequest){
//        subscription = exerciseApi.addExercise(exercise)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(
//                { refreshData() },
//                { onRetrievePostListError() }
//            )
//
//    }
//
    fun refreshData() {
        isRefreshing = true
        loadWorkouts()
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

    private fun onRetrievePostListSuccess(workoutList: WorkoutList) {
        Log.d(TAG, "onRetrievePostListSuccess: " + workoutList.workouts.size.toString())
        workoutListAdapter.updateWorkoutList(
            workoutList.workouts.associateBy(
                keySelector = { it.date },
                valueTransform = { it }).keys.toList()
        )
    }

    private fun onRetrievePostListError() {
//        errorMessage.value = R.string.exercise_error
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }
}