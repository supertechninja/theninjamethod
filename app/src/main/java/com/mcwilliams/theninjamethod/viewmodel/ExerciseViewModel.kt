package com.mcwilliams.theninjamethod.viewmodel

import androidx.lifecycle.MutableLiveData
import com.mcwilliams.theninjamethod.model.Exercise

class ExerciseViewModel : BaseViewModel() {
    private val exerciseTitle = MutableLiveData<String>()
    private val exerciseBody = MutableLiveData<String>()

    fun bind(exercise: Exercise) {
        exerciseTitle.value = exercise.exerciseName
        exerciseBody.value = exercise.exerciseType
    }

    fun getExerciseTitle(): MutableLiveData<String> {
        return exerciseTitle
    }

    fun getExerciseBody(): MutableLiveData<String> {
        return exerciseBody
    }
}