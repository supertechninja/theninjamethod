package com.mcwilliams.theninjamethod.ui.activity.manualworkoutdetail

import android.os.Bundle
import android.view.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.mcwilliams.data.exercisedb.model.WorkoutSet
import com.mcwilliams.data.workoutdb.SimpleWorkout
import com.mcwilliams.data.workoutdb.Workout
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.theme.TheNinjaMethodTheme
import com.mcwilliams.theninjamethod.utils.extensions.fixCase
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

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

        val navController = findNavController()
        workout = arguments?.getSerializable("workout") as SimpleWorkout

        return ComposeView(context = requireContext()).apply {
            setContent {
                TheNinjaMethodTheme {
                    ManualWorkoutDetailContent(
                        navController = navController,
                        workoutId = "",
                        viewModel
                    )
                }
            }
        }
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


}

@Composable
fun ManualWorkoutDetailContent(
    navController: NavController,
    workoutId: String,
    viewModel: ManualWorkoutViewModel
) {
    val workout by viewModel.workout.observeAsState()
    val scrollState = rememberScrollState()
    viewModel.getManualWorkoutDetail(workoutId.toInt())

    workout?.let {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            //TODO add edit name dialog
            Text(
                text = it.workoutName,
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val date = LocalDate.parse(it.workoutDate)
                Text(
                    text =
                    "${date.dayOfWeek.name.fixCase()}, ${date.month.name.fixCase()} ${date.dayOfMonth}, ${date.year}",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                it.workoutDuration?.let { it1 ->
                    Text(
                        text = it1, style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                val totalWeightLifted = totalWeightLifted(it)
                Text(
                    text = if (totalWeightLifted == 0) "" else "$totalWeightLifted lbs",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            workout!!.exercises.forEach { exercise ->
                Card(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Color.LightGray),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = exercise.exerciseName,
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.onSurface,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        exercise.sets.forEach { workoutSet ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Set ${workoutSet.index}",
                                    style = MaterialTheme.typography.body2,
                                    color = MaterialTheme.colors.onSurface,
                                    textAlign = TextAlign.Left
                                )
                                Text(
                                    text = "${workoutSet.weight}lbs x ${workoutSet.reps}",
                                    style = MaterialTheme.typography.body2,
                                    color = MaterialTheme.colors.onSurface,
                                    textAlign = TextAlign.Center
                                )

                                Text(
                                    text = oneRepMax(workoutSet),
                                    style = MaterialTheme.typography.body2,
                                    color = MaterialTheme.colors.onSurface,
                                    textAlign = TextAlign.End
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun oneRepMax(workoutSet: WorkoutSet): String {
    if (workoutSet.weight.isNotEmpty() && workoutSet.reps.isNotEmpty()) {
        val oneRepMax =
            (workoutSet.weight.toInt() / (1.0278 - (0.0278 * workoutSet.reps.toInt()))).toInt()
        return oneRepMax.toString() + "lbs"
    }
    return ""
}

internal fun totalWeightLifted(workout: Workout): Int {
    var totalAmountLifted = 0

    workout.exercises.forEach { workoutExercise ->
        workoutExercise.sets.forEach { workoutSet ->
            if (workoutSet.weight.isNotEmpty() && workoutSet.reps.isNotEmpty()) {
                totalAmountLifted += (workoutSet.weight.toInt() * workoutSet.reps.toInt())
            }
        }
    }

    return totalAmountLifted
}