package com.mcwilliams.theninjamethod.ui.exercises.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mcwilliams.theninjamethod.ui.exercises.db.Exercise
import kotlin.random.Random

class ExerciseViewModel : ViewModel() {
    private val exerciseTitle = MutableLiveData<String>()
    private val exercisePerformedCount = MutableLiveData<String>()
    private val exerciseBodyPart = MutableLiveData<String>()

    fun bind(exercise: Exercise) {
        exerciseTitle.value = exercise.exerciseName
        exercisePerformedCount.value = Random.nextInt(0, 10).toString()
        if (!exercise.exerciseBodyPart.isNullOrEmpty()) {
            exerciseBodyPart.value = exercise.exerciseBodyPart
        } else {
            exerciseBodyPart.value = ""
        }
    }

    fun getExerciseTitle(): MutableLiveData<String> {
        return exerciseTitle
    }

    fun getExerciseBody(): MutableLiveData<String> {
        return exercisePerformedCount
    }

    fun getBodyPart(): MutableLiveData<String> {
        return exerciseBodyPart
    }
}