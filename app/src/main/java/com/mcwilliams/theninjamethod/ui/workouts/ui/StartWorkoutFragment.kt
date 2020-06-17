package com.mcwilliams.theninjamethod.ui.workouts.ui

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.model.Exercise
import com.mcwilliams.theninjamethod.ui.workouts.db.Workout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.add_exercise_row_view.*
import kotlinx.android.synthetic.main.add_exercise_sets_row_view.*
import kotlinx.android.synthetic.main.fragment_start_workout.*
import java.time.LocalDate

@AndroidEntryPoint
class StartWorkoutFragment : Fragment() {

    lateinit var exerciseName : String
    var exerciseList = mutableListOf<Exercise>()

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
        add_exercise.setOnClickListener {
            //Show Exercise Picker, after exercise selection add layout
            //Mocking exercise name
            exerciseName = "Bench Press"

            val addExerciseView = layoutInflater.inflate(R.layout.add_exercise_row_view, null)

            val exerciseNameView = addExerciseView.findViewById<MaterialTextView>(R.id.exercise_name)
            exerciseNameView.text = exerciseName
            var setCounter = 0

            val addSet = addExerciseView.findViewById<MaterialButton>(R.id.add_set)
            addSet.setOnClickListener {
                val addWorkoutSet = layoutInflater.inflate(R.layout.add_exercise_sets_row_view, null)

                val setCount = addWorkoutSet.findViewById<MaterialTextView>(R.id.set_count)
                setCounter = setCounter.inc()
                setCount.text = setCounter.toString()

                val exerciseSets = addExerciseView.findViewById<LinearLayout>(R.id.exercise_sets)
                exerciseSets.addView(addWorkoutSet)
            }

            exercise_list.addView(addExerciseView)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.start_workout_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (R.id.menu_done == item.itemId) {
//            getWorkoutObject()
        }
        return super.onOptionsItemSelected(item)
    }

//    fun getWorkoutObject() : Workout {
//        return Workout(0, workout_name.text.toString(), LocalDate.now().toString(),
//        )
//    }
}

