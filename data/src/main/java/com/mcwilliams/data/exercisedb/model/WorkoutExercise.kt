package com.mcwilliams.data.exercisedb.model

data class WorkoutExercise(
    val exerciseName: String,
    val exerciseType: String,
    val exerciseBodyPart: String,
    var definedExerciseType: ExerciseType?,
    var sets: List<WorkoutSet> = listOf()
) {
    constructor(
        workoutName: String,
        exerciseType: ExerciseType,
        sets: List<WorkoutSet> = listOf()
    ) : this(
        workoutName,
        "",
        "workoutDate",
        exerciseType,
        sets
    )
}

data class Data(val exercises: List<WorkoutExercise>)

data class AddExerciseRequest(val exercise: WorkoutExercise)

data class WorkoutSet(val index: Int, var weight: String, var reps: String)