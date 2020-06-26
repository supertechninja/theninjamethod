package com.mcwilliams.theninjamethod.ui.workouts.manualworkoutdetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.google.android.material.textview.MaterialTextView
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.databinding.WorkoutDetailFragmentBinding
import com.mcwilliams.theninjamethod.ui.workouts.combinedworkoutlist.model.Workout
import com.mcwilliams.theninjamethod.utils.extensions.fixCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.workout_detail_fragment.*
import java.time.LocalDate

@AndroidEntryPoint
class ManualWorkoutDetailFragment : Fragment() {

    private lateinit var binding: WorkoutDetailFragmentBinding
    lateinit var workout: Workout
    private val viewModel: ManualWorkoutViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        workout = arguments?.getSerializable("workout") as Workout

        setHasOptionsMenu(true)

        binding = DataBindingUtil.inflate(
            inflater, R.layout.workout_detail_fragment, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.workout.observe(viewLifecycleOwner, Observer {
            Log.d("TAG", "onViewCreated: ${it.workoutName}")

            workout_name.text = it.workoutName

            val date = LocalDate.parse(it.workoutDate)
            workout_date.text =
                "${date.dayOfWeek.name.fixCase()}, ${date.month.name.fixCase()} ${date.dayOfMonth}, ${date.year}"

            for (exercise in it.exercises) {
                val exerciseRow = layoutInflater.inflate(R.layout.workout_detail_sets_row, null)
                val exerciseName =
                    exerciseRow.findViewById<MaterialTextView>(R.id.exercise_name_detail)
                exerciseName.text = exercise.exerciseName
                exercises_and_sets.addView(exerciseRow)

                val setNumber = exerciseRow.findViewById<MaterialTextView>(R.id.set_count_detail)
                val setRepsAndWeight =
                    exerciseRow.findViewById<MaterialTextView>(R.id.reps_and_weight_count)

                setNumber.visibility = View.GONE
                setRepsAndWeight.visibility = View.GONE

                exercise.sets.forEach {
                    val setRow = layoutInflater.inflate(R.layout.workout_detail_sets_row, null)

                    val exerciseNameOnSetRow =
                        setRow.findViewById<MaterialTextView>(R.id.exercise_name_detail)
                    exerciseNameOnSetRow.visibility = View.GONE

                    val setNumber = setRow.findViewById<MaterialTextView>(R.id.set_count_detail)
                    setNumber.text = it.index
                    val setRepsAndWeight =
                        setRow.findViewById<MaterialTextView>(R.id.reps_and_weight_count)
                    setRepsAndWeight.text = "${it.weight} x ${it.reps}"

                    exercises_and_sets.addView(setRow)
                }
            }
        })

        viewModel.getManualWorkoutDetail(workout.id)

        delete_workout.setOnClickListener {
            viewModel.deleteWorkout()
            Navigation.findNavController(it).popBackStack()
        }
    }

}