package com.mcwilliams.theninjamethod.ui.workouts.combinedworkoutlist.model

import com.mcwilliams.theninjamethod.ui.exercises.model.ExerciseType

data class Exercise(
    val exerciseName: String,
    val exerciseType: String,
    val exerciseBodyPart: String,
    var definedExerciseType: ExerciseType?,
    val sets: MutableList<WorkoutSet>?
) {
    constructor(
        workoutName: String,
        exerciseType: ExerciseType,
        sets: MutableList<WorkoutSet>?
    ) : this(
        workoutName,
        "",
        "workoutDate",
        exerciseType,
        sets
    )
}

data class Data(val exercises: List<Exercise>)

data class AddExerciseRequest(val exercise: Exercise)

data class WorkoutSet(val index: Int, var weight: String, var reps: String)