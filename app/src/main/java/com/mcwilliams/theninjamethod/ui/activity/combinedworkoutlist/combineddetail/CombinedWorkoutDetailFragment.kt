package com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist.combineddetail

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.mcwilliams.data.workoutdb.SimpleWorkout
import com.mcwilliams.data.workoutdb.WorkoutType
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.theme.TheNinjaMethodTheme
import com.mcwilliams.theninjamethod.ui.activity.manualworkoutdetail.totalWeightLifted
import com.mcwilliams.theninjamethod.ui.activity.stravadetail.StravaMapWithStats
import com.mcwilliams.theninjamethod.utils.extensions.getDateString
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.util.*

@AndroidEntryPoint
class CombinedWorkoutDetailFragment : Fragment() {
    lateinit var combinedWorkout: Pair<LocalDate, MutableList<SimpleWorkout>>
    private val viewModel: CombinedWorkoutViewModel by viewModels()
    var totalAmountLifted = 0
    lateinit var rootView: ConstraintLayout
    lateinit var workoutCardContainer: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        combinedWorkout =
            arguments?.getSerializable("workoutSummary") as Pair<LocalDate, MutableList<SimpleWorkout>>
        setHasOptionsMenu(true)
        return ComposeView(requireContext()).apply {
            setContent {
                TheNinjaMethodTheme() {
                    CombinedWorkoutFragmentContent(viewModel, combinedWorkout = combinedWorkout)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.strava_workout_detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (R.id.menu_share == item.itemId) {
            val bundle = bundleOf(
                "manualworkout" to combinedWorkout
            )
            Navigation.findNavController(rootView)
                .navigate(R.id.navigate_to_share_combined_workout, bundle)
        }
        return super.onOptionsItemSelected(item)
    }
}

@Composable
fun CombinedWorkoutFragmentContent(
    viewModel: CombinedWorkoutViewModel,
    combinedWorkout: Pair<LocalDate, MutableList<SimpleWorkout>>
) {
    val listOfStravaWorkouts: MutableList<SimpleWorkout> = mutableListOf()
    val listOfManualWorkouts: MutableList<SimpleWorkout> = mutableListOf()
    for (workout in combinedWorkout.second) {
        when (workout.workoutType) {
            WorkoutType.LIFTING -> {
                listOfManualWorkouts.add(workout)
            }
            WorkoutType.STRAVA -> {
                listOfStravaWorkouts.add(workout)
            }
        }
    }

    if (listOfManualWorkouts.isNotEmpty()) {
        if (listOfManualWorkouts.size > 1) {
//            val ids = listOfStravaWorkouts.map { it.id }
//            viewModel.getManualWorkoutDetail(ids)
        } else {
            viewModel.getManualWorkoutDetail(listOfManualWorkouts[0].id)
        }
    }

    if (listOfStravaWorkouts.isNotEmpty()) {
        if (listOfStravaWorkouts.size > 1) {
            val ids = listOfStravaWorkouts.map { it.id }
            viewModel.getMultipleDetailedActivities(ids)
        } else {
            viewModel.getDetailedActivities(listOfStravaWorkouts[0].id)
        }
    }

    val workout by viewModel.workout.observeAsState()
    val stravaWorkout by viewModel.detailedActivity.observeAsState()
    val stravaWorkouts by viewModel.detailedActivities.observeAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = combinedWorkout.first.getDateString(),
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        workout?.let { manualWorkout ->
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(
                        text = manualWorkout.workoutName,
                        style = MaterialTheme.typography.h5,
                        color = MaterialTheme.colors.onSurface
                    )

                    manualWorkout.exercises.forEach { exercise ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                        ) {
                            Text(text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontSize = 20.sp)) {
                                    append("${exercise.exerciseName} : ")
                                }
                                withStyle(style = SpanStyle(fontSize = 14.sp)) {
                                    exercise.sets.forEach { set ->
                                        if (set.weight.toInt() > 0) {
                                            append("${set.reps} x +${set.weight}lbs, ")
                                        } else {
                                            append("${set.reps}, ")
                                        }
                                    }
                                }
                            }, color = MaterialTheme.colors.onSurface)
                        }
                    }

                    val totalWeightLifted = totalWeightLifted(manualWorkout)
                    Text(
                        text = if (totalWeightLifted == 0) "" else "Total Weight Lifted: $totalWeightLifted lbs",
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        stravaWorkout?.let {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = it.name,
                            style = MaterialTheme.typography.h5,
                            color = MaterialTheme.colors.onSurface
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_heart_icon),
                                contentDescription = "",
                                tint = Color.Red,
                                modifier = Modifier
                                    .size(20.dp)
                                    .padding(end = 4.dp)
                            )
                            Text(
                                text = "${it.average_heartrate} bpm",
                                style = MaterialTheme.typography.body2,
                                color = Color.White,
                                modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
                            )
                        }
                    }
                    StravaMapWithStats(stravaDetailActivity = it)
                }
            }
        }

        stravaWorkouts?.let { listOfStravaDetailActivities ->
            listOfStravaDetailActivities.forEach { stravaDetailActivity ->
//                StravaMapWithStats(stravaDetailActivity = stravaDetailActivity)
            }
        }
    }

}