package com.mcwilliams.theninjamethod.ui.startworkout

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcwilliams.theninjamethod.ui.exercises.db.Exercise
import com.mcwilliams.theninjamethod.ui.exercises.model.ExerciseType
import com.mcwilliams.theninjamethod.ui.exercises.repository.ExerciseRepository
import com.mcwilliams.theninjamethod.ui.ext.toLiveData
import com.mcwilliams.theninjamethod.ui.workouts.combinedworkoutlist.model.WorkoutSet
import com.mcwilliams.theninjamethod.ui.workouts.manualworkoutdetail.ManualWorkoutsRepository
import com.mcwilliams.theninjamethod.ui.workouts.manualworkoutdetail.db.Workout
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import java.time.LocalDate

class StartWorkoutViewModel @ViewModelInject constructor(
    private val manualWorkoutsRepository: ManualWorkoutsRepository,
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {

    //List of exercises from DB to add to a workout
    var listOfExercises: LiveData<List<Exercise>>

    var _didSaveWorkout = MutableLiveData<Boolean>()
    var didSaveWorkout: LiveData<Boolean> = _didSaveWorkout

    private var _exercise = MutableLiveData<String>()
    var exercise: MutableLiveData<String> = _exercise

    var _workout = MutableLiveData<Workout>()
    var workout: MutableLiveData<Workout> = _workout

    var workoutInProgress: Workout? = null
    var listOfExercisesPerformed: MutableList<com.mcwilliams.theninjamethod.ui.workouts.combinedworkoutlist.model.Exercise> =
        mutableListOf()

    var compositeDisposable = CompositeDisposable()

    init {
        listOfExercises = exerciseRepository.getExercises()!!.toObservable()
            .toLiveData(compositeDisposable) { it }

    }

    fun saveWorkout() {
        viewModelScope.launch {
            manualWorkoutsRepository.addWorkout(workoutInProgress!!)
            _didSaveWorkout.postValue(true)
            workoutInProgress = null
        }
    }

    //Create empty workout obj
    fun createWorkout(workoutName: String) {
        workoutInProgress = Workout(0, workoutName, LocalDate.now().toString())
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
        val newExercise =
            com.mcwilliams.theninjamethod.ui.workouts.combinedworkoutlist.model.Exercise(
                s,
                definedExerciseType,
                mutableListOf(WorkoutSet(1, "", ""))
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

    //Update set in exercise with values from UI, Finds the exercise in the workout by exerciseName,
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

}