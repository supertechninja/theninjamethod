package com.mcwilliams.theninjamethod.ui.activity.manualworkoutdetail

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.google.android.material.textview.MaterialTextView
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist.model.Workout
import com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist.model.WorkoutSet
import com.mcwilliams.theninjamethod.ui.exercises.model.ExerciseType
import com.mcwilliams.theninjamethod.utils.extensions.fixCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.workout_detail_fragment.*
import java.text.NumberFormat
import java.time.LocalDate
import java.util.*

@AndroidEntryPoint
class ManualWorkoutDetailFragment : Fragment() {
    lateinit var workout: Workout
    lateinit var detailedWorkout: com.mcwilliams.theninjamethod.ui.activity.manualworkoutdetail.db.Workout
    private val viewModel: ManualWorkoutViewModel by viewModels()
    private var totalAmountLifted = 0
    lateinit var rootView: ConstraintLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        workout = arguments?.getSerializable("workout") as Workout
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.workout_detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rootView = view.findViewById(R.id.rootView)

        viewModel.workout.observe(viewLifecycleOwner, Observer {
            Log.d("TAG", "onViewCreated: ${it.workoutName}")
            detailedWorkout = it
            workout_name.text = it.workoutName

            val date = LocalDate.parse(it.workoutDate)
            workout_date.text =
                "${date.dayOfWeek.name.fixCase()}, ${date.month.name.fixCase()} ${date.dayOfMonth}, ${date.year}"

            val workoutDuration = view.findViewById<MaterialTextView>(R.id.workout_duration)
            workoutDuration.text = "Duration: ${it.workoutDuration}"

            exercises_and_sets.removeAllViews()
            for (exercise in it.exercises!!) {

                val exerciseRow =
                    layoutInflater.inflate(R.layout.workout_detail_exercise_header_row, null)
                val exerciseName =
                    exerciseRow.findViewById<MaterialTextView>(R.id.exercise_name_detail)
                exerciseName.text = exercise.exerciseName
                exercises_and_sets.addView(exerciseRow)

                totalWeightLifted(exercise.sets!!)

                exercise.sets.forEach { exerciseSet ->
                    val setRow = layoutInflater.inflate(R.layout.workout_detail_sets_row, null)

                    val setNumber = setRow.findViewById<MaterialTextView>(R.id.set_count_detail)
                    setNumber.text = exerciseSet.index.toString()
                    val setRepsAndWeight =
                        setRow.findViewById<MaterialTextView>(R.id.reps_and_weight_count)

                    when (exercise.definedExerciseType) {
                        ExerciseType.bodyweight -> {
                            if (exerciseSet.weight.toInt() > 0) {
                                setRepsAndWeight.text =
                                    "+${exerciseSet.weight}lbs x ${exerciseSet.reps}"
                            } else {
                                setRepsAndWeight.text = "${exerciseSet.reps} reps"
                            }
                        }
                        else -> {
                            setRepsAndWeight.text = "${exerciseSet.weight}lbs x ${exerciseSet.reps}"
                        }
                    }

                    val onRepMaxTextView =
                        setRow.findViewById<MaterialTextView>(R.id.one_rep_max_value)

                    if (exerciseSet.weight.isNotEmpty() && exerciseSet.reps.isNotEmpty()) {
                        val oneRepMax =
                            (exerciseSet.weight.toInt() / (1.0278 - (0.0278 * exerciseSet.reps.toInt()))).toInt()
                        onRepMaxTextView.text = oneRepMax.toString() + "lbs"
                    }

                    exercises_and_sets.addView(setRow)
                }
            }
            total_weight_lifted.text =
                NumberFormat.getNumberInstance(Locale.US).format(totalAmountLifted) + "lbs lifted"
        })

        viewModel.getManualWorkoutDetail(workout.id)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.manual_workout_detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (R.id.menu_delete == item.itemId) {
            viewModel.deleteWorkout()
            Navigation.findNavController(rootView).popBackStack()
        } else if (R.id.menu_share == item.itemId) {
            val bundle = bundleOf(
                "workout" to detailedWorkout,
                "amountLifted" to totalAmountLifted
            )
            Navigation.findNavController(rootView)
                .navigate(R.id.navigate_to_share_workout, bundle)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun totalWeightLifted(sets: List<WorkoutSet>) {
        sets.forEach {
            if (it.weight.isNotEmpty() && it.reps.isNotEmpty()) {
                totalAmountLifted += (it.weight.toInt() * it.reps.toInt())
            }
        }
    }
}