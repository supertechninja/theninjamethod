package com.mcwilliams.theninjamethod.ui.startworkout

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mcwilliams.data.exercisedb.DbExercise
import com.mcwilliams.theninjamethod.ui.exercises.AddExerciseDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseExerciseDialogFragment(private val listOfExercise: List<DbExercise>) :
    DialogFragment() {

    private val startWorkoutViewModel: StartWorkoutViewModel by activityViewModels()

    override fun onCreateDialog(
        savedInstanceState: Bundle?
    ): Dialog {

        val exerciseNames = mutableListOf<String>()
        exerciseNames.add(0, "New")
        for (exercise in listOfExercise) {
            exerciseNames.add(exercise.exerciseName)
        }
        val arrayOfExercises = exerciseNames.toTypedArray()

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Choose Exercise")
            .setItems(arrayOfExercises) { _, which ->
                //Set the name of the exercise in the UI
                if (arrayOfExercises[which] == "New") {
                    AddExerciseDialog().show(parentFragmentManager, "TAG")
                } else {
                    val exercise = arrayOfExercises[which]
                    val exerciseObj = listOfExercise.find { it.exerciseName == exercise }
                    startWorkoutViewModel.addExercise(
                        exerciseObj!!.exerciseName,
//                        exerciseObj.definedExerciseType!!
                    )
                }
            }
            // Add customization options here
            .create()
    }
}