package com.mcwilliams.theninjamethod.ui.workouts.combinedworkoutlist.model

data class Exercise(
    val exerciseName: String,
    val exerciseType: String,
    val exerciseBodyPart: String,
    val sets: MutableList<WorkoutSet>?
) {
    constructor(workoutName: String, sets: MutableList<WorkoutSet>?) : this(
        workoutName,
        "",
        "workoutDate",
        sets
    )
}

data class Data(val exercises: List<Exercise>)

data class AddExerciseRequest(val exercise: Exercise)

data class WorkoutSet(val index: Int, var weight: String, var reps: String)