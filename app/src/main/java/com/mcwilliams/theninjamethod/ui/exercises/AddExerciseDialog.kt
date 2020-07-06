package com.mcwilliams.theninjamethod.ui.exercises

import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.ui.exercises.db.Exercise
import com.mcwilliams.theninjamethod.ui.exercises.model.ExerciseType
import com.mcwilliams.theninjamethod.ui.exercises.viewmodel.ExerciseListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddExerciseDialog : DialogFragment() {

    private val viewModel: ExerciseListViewModel by viewModels()

    override fun onCreateDialog(
        savedInstanceState: Bundle?
    ): Dialog {

        val exerciseDialog = layoutInflater.inflate(R.layout.add_exercise_dialog, null)

        val exerciseTypes = ExerciseType.values()
        val exerciseTypeNames = mutableListOf<String>()

        exerciseTypes.forEach { exerciseType ->
            exerciseTypeNames.add(exerciseType.exerciseType)
        }

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.dropdown_menu_item,
            exerciseTypeNames
        )

        val editTextFilledExposedDropdown: AutoCompleteTextView =
            exerciseDialog.findViewById(R.id.filled_exposed_dropdown)
        editTextFilledExposedDropdown.setAdapter(adapter)

        var exerciseTypeSelected = ""
        editTextFilledExposedDropdown.setOnItemClickListener { adapterView, view, i, l ->
            exerciseTypeSelected = exerciseTypeNames[i]
        }

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Add Exercise")
            .setView(exerciseDialog)
            .setPositiveButton("Save") { _, _ ->
                val exerciseName =
                    exerciseDialog.findViewById<TextInputEditText>(R.id.exerciseName).text.toString()

                val exerciseBodyPart =
                    exerciseDialog.findViewById<TextInputEditText>(R.id.exerciseBodyPart).text.toString()

                val exercise = Exercise(
                    0,
                    exerciseName,
                    exerciseTypeSelected,
                    exerciseBodyPart,
                    ExerciseType.valueOf(exerciseTypeSelected)
                )
                viewModel.addNewExercise(exercise)
            }
            .setNegativeButton("Cancel") { _, _ -> }
            // Add customization options here
            .create()
    }
}