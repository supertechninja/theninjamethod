package com.mcwilliams.theninjamethod.ui.workouts.combinedworkoutlist.model

data class Exercise(
    val exerciseName: String,
    val exerciseType: String,
    val exerciseBodyPart: String,
    val sets: List<WorkoutSet>
)

data class Data(val exercises: List<Exercise>)

data class AddExerciseRequest(val exercise: Exercise)

data class WorkoutSet (val index: String, val weight:String, val reps: String)