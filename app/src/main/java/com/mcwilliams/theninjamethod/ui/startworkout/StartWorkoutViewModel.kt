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
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import java.time.LocalDate

class StartWorkoutViewModel @ViewModelInject constructor(
    private val manualWorkoutsRepository: ManualWorkoutsRepository,
    exerciseRepository: ExerciseRepository,
    private val routinesRepository: RoutinesRepository
) : ViewModel() {

    //List of exercises from DB to add to a workout
    var listOfExercises: LiveData<List<DbExercise>>

    var _didSaveWorkout = MutableLiveData(false)
    var didSaveWorkout: LiveData<Boolean> = _didSaveWorkout

    var _workout = MutableLiveData(Workout(0, "", LocalDate.now().toString()))
    var workout: LiveData<Workout> = _workout

    var compositeDisposable = CompositeDisposable()

    init {
        listOfExercises = exerciseRepository.getExercises()!!.toObservable()
            .toLiveData(compositeDisposable) { it }
    }

    fun saveWorkout(workoutDuration: String, saveWorkoutAsRoutine: Boolean) {
        val completedWorkout = workout.value!!
        completedWorkout.workoutDuration = workoutDuration
        completedWorkout.caloriesBurned = ""
        //caloriesBurned(3.5f, 180, time.minute).toString()
        completedWorkout.workoutTotalWeight = calculateTotalWeightLifted().toString()
        viewModelScope.launch {
            manualWorkoutsRepository.addWorkout(workout = completedWorkout)
//            if (saveWorkoutAsRoutine) {
//                routinesRepository.addRoutine(workoutInProgress!!)
//            }
            _didSaveWorkout.postValue(true)
            //Reset workout object in view model
            val workout = Workout(0, "", "", listOf())
            _workout.postValue(workout)
        }
    }

    fun addExercise(exerciseName: String) {
        //make a copy of the existing exercises
        val existingExercies = workout.value!!.exercises
        //create new exercise
        val workoutExercise =
            WorkoutExercise(exerciseName, ExerciseType.bodyweight, mutableListOf())
        // merge exercises into 1 list
        val combinedExercises = existingExercies + listOf(workoutExercise)
        //create new workout object with combined exercies
        val updatedWorkout = Workout(
            workout.value!!.id,
            workout.value!!.workoutName,
            workout.value!!.workoutDate,
            combinedExercises
        )
        //post value of new workout
        _workout.postValue(updatedWorkout)
    }

    fun addSetToExercise(setCount: Int, exerciseName: String) {
        //store the current workout sets
        val currentSets =
            workout.value!!.exercises.find { it.exerciseName == exerciseName }!!.sets
        //create the new workout set
        val newSet = WorkoutSet(setCount, "", "")
        //store the current exercises
        val existingExercies = workout.value!!.exercises
        //find the current exercise to update the sets on
        val currentExercise = existingExercies.find { it.exerciseName == exerciseName }
        //create a mutable list of existing exercises
        val list = existingExercies.toMutableList()
        //create an update the current exercise with an updated sets list
        list[existingExercies.indexOf(currentExercise)] =
            WorkoutExercise(exerciseName, ExerciseType.bodyweight, currentSets + listOf(newSet))
        //create a new workout object with the updated workout with the updated sets
        val updatedWorkout = Workout(
            workout.value!!.id,
            workout.value!!.workoutName,
            workout.value!!.workoutDate,
            list.toList()
        )
        //post updated value of workout
        _workout.postValue(updatedWorkout)
    }

//    fun createWorkoutFromRoutine(workout: Workout) {
//        workoutInProgress = workout
//        _workout.postValue(workoutInProgress)
//    }

    fun updateWorkoutName(workoutName: String) {
        val currentWorkout = workout.value
        currentWorkout!!.workoutName = workoutName
        _workout.postValue(currentWorkout)
    }

    fun cancelWorkout() {
        _didSaveWorkout.postValue(true)
        //Reset workout object in view model
        val workout = Workout(0, "", "", listOf())
        _workout.postValue(workout)
    }

    fun calculateTotalWeightLifted(): Int {
        var totalWeight = 12000
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