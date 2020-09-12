package com.mcwilliams.theninjamethod.ui.startworkout

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcwilliams.data.ManualWorkoutsRepository
import com.mcwilliams.data.exercisedb.DbExercise
import com.mcwilliams.data.exercisedb.model.ExerciseType
import com.mcwilliams.data.exercisedb.model.WorkoutExercise
import com.mcwilliams.data.exercisedb.model.WorkoutSet
import com.mcwilliams.data.workoutdb.Workout
import com.mcwilliams.theninjamethod.ui.exercises.repository.ExerciseRepository
import com.mcwilliams.theninjamethod.ui.ext.toLiveData
import com.mcwilliams.theninjamethod.ui.routines.RoutinesRepository
import com.mcwilliams.theninjamethod.utils.extensions.caloriesBurned
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

class StartWorkoutViewModel @ViewModelInject constructor(
    private val manualWorkoutsRepository: ManualWorkoutsRepository,
    exerciseRepository: ExerciseRepository,
    private val routinesRepository: RoutinesRepository
) : ViewModel() {

    //List of exercises from DB to add to a workout
    var listOfExercises: LiveData<List<DbExercise>>

    var _didSaveWorkout = MutableLiveData<Boolean>()
    var didSaveWorkout: LiveData<Boolean> = _didSaveWorkout

    private var _exercise = MutableLiveData<String>()
    var exercise: MutableLiveData<String> = _exercise

    var _workout = MutableLiveData<com.mcwilliams.data.workoutdb.Workout>()
    var workout: MutableLiveData<com.mcwilliams.data.workoutdb.Workout> = _workout

    var workoutInProgress: com.mcwilliams.data.workoutdb.Workout? = null
    var listOfExercisesPerformed: MutableList<WorkoutExercise> =
        mutableListOf()

    var compositeDisposable = CompositeDisposable()

    init {
        listOfExercises = exerciseRepository.getExercises()!!.toObservable()
            .toLiveData(compositeDisposable) { it }
    }

    fun saveWorkout(workoutDuration: String) {
        val time = LocalTime.parse(workoutDuration)
        workoutInProgress!!.workoutDuration = workoutDuration
        workoutInProgress!!.caloriesBurned = caloriesBurned(3.5f, 180, time.minute).toString()
        workoutInProgress!!.workoutTotalWeight = calculateTotalWeightLifted().toString()
        viewModelScope.launch {
            manualWorkoutsRepository.addWorkout(workoutInProgress!!)
            routinesRepository.addRoutine(workoutInProgress!!)
            _didSaveWorkout.postValue(true)
            listOfExercisesPerformed.clear()
            workoutInProgress = null
        }
    }

    //Create empty workout obj
    fun createWorkout(workoutName: String) {
        workoutInProgress = Workout(0, workoutName, LocalDate.now().toString())
        _workout.postValue(workoutInProgress)
    }

    fun createWorkoutFromRoutine(workout: Workout) {
        workoutInProgress = workout
        _workout.postValue(workoutInProgress)
    }

    fun updateWorkoutName(workoutName: String) {
        workoutInProgress!!.workoutName = workoutName
        _workout.postValue(workoutInProgress)
    }

    //Create an exercise with an empty list of sets
    fun addExerciseToWorkout(
        s: String,
        definedExerciseType: ExerciseType
    ) {
        var startingWeight = ""
        if (definedExerciseType == ExerciseType.bodyweight) {
            startingWeight = "0"
        }

        val newExercise =
            WorkoutExercise(
                s,
                definedExerciseType,
                mutableListOf(WorkoutSet(1, startingWeight, ""))
            )
        listOfExercisesPerformed.add(newExercise)
        workoutInProgress!!.exercises = listOfExercisesPerformed
        _workout.postValue(workoutInProgress)
    }

    //Create empty workout set in exercise to update later
    fun addNewSetToExerciseToWorkout(exercise: String) {
        val exerciseToAddSetTo =
            workoutInProgress!!.exercises!!.find { it.exerciseName == exercise }
        val currentSetCount = exerciseToAddSetTo!!.sets!!.size
        val newSet = WorkoutSet(currentSetCount.inc(), "", "")
        workoutInProgress!!.exercises!!.find { it.exerciseName == exercise }!!.sets!!.add(newSet)
        _workout.postValue(workoutInProgress)
    }

    // Update set in exercise with values from UI, Finds the exercise in the workout by exerciseName,
    // then finds the sets based on index
    fun updateSetInExercise(index: Int, weight: String, reps: String, exercise: String) {
        workoutInProgress!!.exercises!!.find { it.exerciseName == exercise }!!
            .sets!!.find { it.index == index }!!.weight = weight
        workoutInProgress!!.exercises!!.find { it.exerciseName == exercise }!!
            .sets!!.find { it.index == index }!!.reps = reps
        _workout.postValue(workoutInProgress)
    }

    fun cancelWorkout() {
        workoutInProgress = null
        _workout.postValue(workoutInProgress)
        _didSaveWorkout.postValue(true)
    }

    fun calculateTotalWeightLifted(): Int {
        var totalWeight = 0
//        workoutInProgress!!.exercises!!.forEach { exercise ->
//            exercise.sets!!.forEach {
//                totalWeight += if (exercise.definedExerciseType == ExerciseType.bodyweight) {
//                    //TODO read BW from profile
//                    (180 * it.reps.toInt())
//                } else {
//                    (it.weight.toInt() * it.reps.toInt())
//                }
//            }
//        }
        return totalWeight
    }

}