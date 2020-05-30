package com.mcwilliams.theninjamethod.ui.exercises.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mcwilliams.theninjamethod.model.Exercise

class ExerciseViewModel : ViewModel() {
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