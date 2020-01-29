package com.mcwilliams.theninjamethod.model

data class Exercise(
    val exerciseName: String,
    val exerciseType: String,
    val exerciseBodyPart: String
)

data class Data(val exercises: List<Exercise>)

data class AddExerciseRequest(val exercise: Exercise)

data class Response(val updated: Int)