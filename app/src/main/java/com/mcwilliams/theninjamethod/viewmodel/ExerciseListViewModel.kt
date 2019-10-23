package com.mcwilliams.theninjamethod.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.model.Exercise
import com.mcwilliams.theninjamethod.network.ExerciseApi
import com.mcwilliams.theninjamethod.ui.home.ExerciseListAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ExerciseListViewModel:BaseViewModel(){
    @Inject
    lateinit var exerciseApi: ExerciseApi

    private lateinit var subscription: Disposable

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    val errorMessage:MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadExercises() }

    val exerciseListAdapter: ExerciseListAdapter = ExerciseListAdapter()

    init{
        loadExercises()
    }

    private fun loadExercises(){
        subscription = exerciseApi.getExercises()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRetrievePostListStart() }
            .doOnTerminate { onRetrievePostListFinish() }
            .subscribe(
                { result -> onRetrievePostListSuccess(result) },
                { onRetrievePostListError() }
            )
    }

    private fun onRetrievePostListStart(){
        loadingVisibility.value = View.VISIBLE
        errorMessage.value = null
    }

    private fun onRetrievePostListFinish(){
        loadingVisibility.value = View.GONE
    }

    private fun onRetrievePostListSuccess(exerciseList:List<Exercise>){
        exerciseListAdapter.updatePostList(exerciseList)
    }

    private fun onRetrievePostListError(){
        errorMessage.value = R.string.exercise_error
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }
}