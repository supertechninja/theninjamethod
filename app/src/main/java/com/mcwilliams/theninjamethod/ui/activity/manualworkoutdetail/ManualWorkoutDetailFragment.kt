package com.mcwilliams.theninjamethod.ui.activity.manualworkoutdetail

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.composethemeadapter.MdcTheme
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.mcwilliams.data.exercisedb.model.ExerciseType
import com.mcwilliams.data.exercisedb.model.WorkoutSet
import com.mcwilliams.data.workoutdb.SimpleWorkout
import com.mcwilliams.data.workoutdb.Workout
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.theme.TheNinjaMethodTheme
import com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist.ActivityContentScaffold
import com.mcwilliams.theninjamethod.utils.extensions.fixCase
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.time.LocalDate
import java.util.*

@AndroidEntryPoint
class ManualWorkoutDetailFragment : Fragment() {
    lateinit var workout: SimpleWorkout
    lateinit var detailedWorkout: Workout
    private val viewModel: ManualWorkoutViewModel by viewModels()
    private var totalAmountLifted = 0
    lateinit var rootView: ConstraintLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
//        return inflater.inflate(R.layout.workout_detail_fragment, container, false)

        val navController = findNavController()
        workout = arguments?.getSerializable("workout") as SimpleWorkout

        return ComposeView(context = requireContext()).apply {
            setContent {
                MdcTheme {
                    ManualWorkoutDetailContent(navController = navController, workoutArg = workout)
                }
            }
        }
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        rootView = view.findViewById(R.id.rootView)
//
//        viewModel.workout.observe(viewLifecycleOwner, Observer { workout ->
//            Log.d("TAG", "onViewCreated: ${workout.workoutName}")
//            detailedWorkout = workout
//            val workoutName = view.findViewById<MaterialTextView>(R.id.workout_name)
//            workoutName.text = workout.workoutName
//            workoutName.setOnClickListener {
//                val inputView = layoutInflater.inflate(R.layout.workout_name_dialog, null)
//                val duration = inputView.findViewById<TextInputEditText>(R.id.tilWorkoutName)
//                duration.setText(detailedWorkout.workoutName)
//
//                MaterialAlertDialogBuilder(requireContext())
//                    .setTitle("Edit Workout Name")
//                    .setView(inputView)
//                    .setPositiveButton("Save") { _, _ ->
//                        workoutName.text = duration.text.toString()
//                        detailedWorkout.workoutName = duration.text.toString()
//                        viewModel.updateWorkout(detailedWorkout)
//                    }
//                    .setNegativeButton("Cancel") { _, _ -> }
//                    // Add customization options here
//                    .create().show()
//            }
//
//            val workoutDate = view.findViewById<MaterialTextView>(R.id.workout_date)
//            val date = LocalDate.parse(workout.workoutDate)
//            workoutDate.text =
//                "${date.dayOfWeek.name.fixCase()}, ${date.month.name.fixCase()} ${date.dayOfMonth}, ${date.year}"
//
//            val workoutDuration = view.findViewById<MaterialTextView>(R.id.workout_duration)
//            workoutDuration.text = "Duration: ${workout.workoutDuration}"
//
//            val exercisesAndSets = view.findViewById<LinearLayout>(R.id.exercises_and_sets)
//            exercisesAndSets.removeAllViews()
//            for (exercise in workout.exercises!!) {
//
//                val exerciseRow =
//                    layoutInflater.inflate(R.layout.workout_detail_exercise_header_row, null)
//                val exerciseName =
//                    exerciseRow.findViewById<MaterialTextView>(R.id.exercise_name_detail)
//                exerciseName.text = exercise.exerciseName
//                exercisesAndSets.addView(exerciseRow)
//
//                totalWeightLifted(exercise.sets!!)
//
//                exerciseRow.setOnLongClickListener {
////                    workout.exercises!!.remove(exercise)
////                    viewModel.updateWorkout(workout)
////                    Toast.makeText(
////                        it.context,
////                        "${exercise.exerciseName} removed",
////                        Toast.LENGTH_SHORT
////                    ).show()
//                    true
//                }
//
//                exercise.sets!!.forEach { exerciseSet ->
//                    val setRow = layoutInflater.inflate(R.layout.workout_detail_sets_row, null)
//
//                    val setNumber = setRow.findViewById<MaterialTextView>(R.id.set_count_detail)
//                    setNumber.text = exerciseSet.index.toString()
//                    val setRepsAndWeight =
//                        setRow.findViewById<MaterialTextView>(R.id.reps_and_weight_count)
//
//                    when (exercise.definedExerciseType) {
//                        ExerciseType.bodyweight -> {
//                            if (exerciseSet.weight.toInt() > 0) {
//                                setRepsAndWeight.text =
//                                    "+${exerciseSet.weight}lbs x ${exerciseSet.reps}"
//                            } else {
//                                setRepsAndWeight.text = "${exerciseSet.reps} reps"
//                            }
//                        }
//                        else -> {
//                            setRepsAndWeight.text = "${exerciseSet.weight}lbs x ${exerciseSet.reps}"
//                        }
//                    }
//
//                    val onRepMaxTextView =
//                        setRow.findViewById<MaterialTextView>(R.id.one_rep_max_value)
//
//                    if (exerciseSet.weight.isNotEmpty() && exerciseSet.reps.isNotEmpty()) {
//                        val oneRepMax =
//                            (exerciseSet.weight.toInt() / (1.0278 - (0.0278 * exerciseSet.reps.toInt()))).toInt()
//                        onRepMaxTextView.text = oneRepMax.toString() + "lbs"
//                    }
//
//                    exercisesAndSets.addView(setRow)
//                }
//            }
//            val totalWeightLifted = view.findViewById<MaterialTextView>(R.id.total_weight_lifted)
//            totalWeightLifted.text =
//                NumberFormat.getNumberInstance(Locale.US).format(totalAmountLifted) + "lbs lifted"
//        })
//
//        viewModel.getManualWorkoutDetail(workout.id)
//    }

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

@Composable
fun ManualWorkoutDetailContent(navController: NavController, workoutArg: SimpleWorkout) {
    val viewModel = viewModel(modelClass = ManualWorkoutViewModel::class.java)
    val workout by viewModel.workout.observeAsState()

    viewModel.getManualWorkoutDetail(workoutArg.id)

    workout?.let {
        Column(modifier = Modifier.padding(16.dp)) {
            //TODO add edit name dialog
            Text(text = it.workoutName, style = MaterialTheme.typography.h6, color = Color.White)

            Row {
                val date = LocalDate.parse(it.workoutDate)
                Text(
                    text =
                    "${date.dayOfWeek.name.fixCase()}, ${date.month.name.fixCase()} ${date.dayOfMonth}, ${date.year}",
                    style = MaterialTheme.typography.body1,
                    color = Color.White,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                it.workoutDuration?.let { it1 -> Text(text = it1) }
            }

            LazyColumnFor(items = it.exercises) { exercise ->

                Card(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, Color.LightGray),
                    modifier = Modifier.padding(8.dp)
                ) {

                    Text(
                        text = exercise.exerciseName,
                        modifier = Modifier.padding(vertical = 8.dp),
                        style = MaterialTheme.typography.body1,
                        color = Color.White
                    )

                    exercise.sets.forEach { workoutSet ->
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text(
                                text = "${workoutSet.index}",
                                style = MaterialTheme.typography.body2,
                                color = Color.White
                            )
                            Text(
                                text = "${workoutSet.weight}lbs x ${workoutSet.reps}",
                                style = MaterialTheme.typography.body2,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }


}