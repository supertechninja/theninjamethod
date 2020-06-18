package com.mcwilliams.theninjamethod.ui.startworkout

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.ui.workouts.db.Workout
import com.mcwilliams.theninjamethod.ui.workouts.model.Exercise
import com.mcwilliams.theninjamethod.ui.workouts.model.WorkoutSet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_start_workout.*
import java.time.LocalDate


@AndroidEntryPoint
class StartWorkoutFragment : Fragment() {

    lateinit var exerciseName: String
    lateinit var exerciseListView: LinearLayout
    lateinit var loadedExercises: List<com.mcwilliams.theninjamethod.ui.exercises.db.Exercise>

    private val startWorkoutViewModel: StartWorkoutViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_start_workout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startWorkoutViewModel.listOfExercises.observe(viewLifecycleOwner, Observer { exercistList ->
            if (!exercistList.isNullOrEmpty()) {
                loadedExercises = exercistList
            }
        })

        //kotlin synthetics for accessing root views
        val addExercise = view.findViewById<ExtendedFloatingActionButton>(R.id.add_exercise)
        addExercise.setOnClickListener {
            exerciseName = "SELECT EXERCISE"

            val addExerciseViewLayout = layoutInflater.inflate(R.layout.add_exercise_row_view, null)
            val exerciseNameView =
                addExerciseViewLayout.findViewById<MaterialTextView>(R.id.exercise_name)
            exerciseNameView.text = exerciseName

            exerciseNameView.setOnClickListener {
                //Setup exercise picker dialog
                val builder: AlertDialog.Builder = AlertDialog.Builder(it.context)
                builder.setTitle("Choose Exercise")
                val exerciseNames = mutableListOf<String>()
                for (exercise in loadedExercises) {
                    exerciseNames.add(exercise.exerciseName)
                }
                val arrayOfExercises = exerciseNames.toTypedArray()
                builder.setItems(arrayOfExercises) { _, which ->
                    //Set the name of the exercise in the UI
                    exerciseNameView.text = arrayOfExercises[which]
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }

            var setCounter = 0

            val addSet = addExerciseViewLayout.findViewById<MaterialButton>(R.id.add_set)
            addSet.setOnClickListener {
                val addWorkoutSetLayout =
                    layoutInflater.inflate(R.layout.add_exercise_sets_row_view, null)

                val setCount = addWorkoutSetLayout.findViewById<MaterialTextView>(R.id.set_count)
                setCounter = setCounter.inc()
                setCount.text = setCounter.toString()

                val exerciseSets =
                    addExerciseViewLayout.findViewById<LinearLayout>(R.id.exercise_sets)
                exerciseSets.addView(addWorkoutSetLayout)
            }

            exerciseListView = view.findViewById(R.id.exercise_list)
            exerciseListView.addView(addExerciseViewLayout)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.start_workout_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (R.id.menu_done == item.itemId) {
            val workoutToAdd = getWorkoutObject()
            val didSave = startWorkoutViewModel.saveWorkout(workoutToAdd)
            if (didSave) goBack()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun goBack() {
        Navigation.findNavController(exerciseListView).popBackStack()
    }

    private fun getWorkoutObject(): Workout {
        val exerciseList = mutableListOf<Exercise>()
        for (exerciseRow in exerciseListView.children) {
            val exerciseName = exerciseRow.findViewById<MaterialTextView>(R.id.exercise_name)

            val sets = mutableListOf<WorkoutSet>()
            val setsRowView = exerciseRow.findViewById<LinearLayout>(R.id.exercise_sets)

            for (setsRow in setsRowView.children) {
                val setCount = setsRow.findViewById<MaterialTextView>(R.id.set_count)
                val weight = setsRow.findViewById<TextInputEditText>(R.id.weight_amount)
                val reps = setsRow.findViewById<TextInputEditText>(R.id.rep_count)

                sets.add(
                    WorkoutSet(
                        setCount.text.toString(),
                        weight.text.toString(),
                        reps.text.toString()
                    )
                )
            }

            val exercise =
                Exercise(
                    exerciseName.text.toString(),
                    "",
                    "",
                    sets
                )
            exerciseList.add(exercise)
        }

        return Workout(0, workout_name.text.toString(), LocalDate.now().toString(), exerciseList)
    }
}

