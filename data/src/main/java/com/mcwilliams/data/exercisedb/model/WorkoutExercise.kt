package com.mcwilliams.data.exercisedb.model

data class WorkoutExercise(
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

data class Data(val exercises: List<WorkoutExercise>)

data class AddExerciseRequest(val exercise: WorkoutExercise)

data class WorkoutSet(val index: Int, var weight: String, var reps: String)