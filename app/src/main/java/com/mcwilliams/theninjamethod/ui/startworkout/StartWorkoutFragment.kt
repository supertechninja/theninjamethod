package com.mcwilliams.theninjamethod.ui.startworkout

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.model.Exercise
import com.mcwilliams.theninjamethod.model.WorkoutSet
import com.mcwilliams.theninjamethod.ui.workouts.WorkoutListViewModel
import com.mcwilliams.theninjamethod.ui.workouts.db.Workout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_start_workout.*
import java.time.LocalDate

@AndroidEntryPoint
class StartWorkoutFragment : Fragment() {

    lateinit var exerciseName : String
    lateinit var exerciseListView : LinearLayout

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

        //kotlin synthetics for accessing root views
        val addExercise =  view.findViewById<ExtendedFloatingActionButton>(R.id.add_exercise)
        addExercise.setOnClickListener {
            //Show Exercise Picker, after exercise selection add layout
            //Mocking exercise name
            exerciseName = "Bench Press"

            val addExerciseViewLayout = layoutInflater.inflate(R.layout.add_exercise_row_view, null)

            val exerciseNameView = addExerciseViewLayout.findViewById<MaterialTextView>(R.id.exercise_name)
            exerciseNameView.text = exerciseName
            var setCounter = 0

            val addSet = addExerciseViewLayout.findViewById<MaterialButton>(R.id.add_set)
            addSet.setOnClickListener {
                val addWorkoutSetLayout = layoutInflater.inflate(R.layout.add_exercise_sets_row_view, null)

                val setCount = addWorkoutSetLayout.findViewById<MaterialTextView>(R.id.set_count)
                setCounter = setCounter.inc()
                setCount.text = setCounter.toString()

                val exerciseSets = addExerciseViewLayout.findViewById<LinearLayout>(R.id.exercise_sets)
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

    private fun goBack(){
        Navigation.findNavController(exerciseListView).popBackStack()
    }

    private fun getWorkoutObject(): Workout {
        val exerciseList = mutableListOf<Exercise>()
        for (exerciseRow in exerciseListView.children){
            val exerciseName = exerciseRow.findViewById<MaterialTextView>(R.id.exercise_name)

            val sets = mutableListOf<WorkoutSet>()
            val setsRowView = exerciseRow.findViewById<LinearLayout>(R.id.exercise_sets)

            for (setsRow in setsRowView.children){
                val setCount = setsRow.findViewById<MaterialTextView>(R.id.set_count)
                val weight = setsRow.findViewById<TextInputEditText>(R.id.weight_amount)
                val reps = setsRow.findViewById<TextInputEditText>(R.id.rep_count)

                sets.add(WorkoutSet(setCount.text.toString(), weight.text.toString(), reps.text.toString()))
            }

            val exercise = Exercise(exerciseName.text.toString(), "", "", sets)
            exerciseList.add(exercise)
        }

        return Workout(0, workout_name.text.toString(), LocalDate.now().toString(),exerciseList)
    }
}

