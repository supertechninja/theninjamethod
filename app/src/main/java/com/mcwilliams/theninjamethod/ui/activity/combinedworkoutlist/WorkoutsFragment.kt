package com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.imageResource
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mcwilliams.data.workoutdb.SimpleWorkout
import com.mcwilliams.data.workoutdb.WorkoutType
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.theme.TheNinjaMethodTheme
import com.mcwilliams.theninjamethod.utils.extensions.getDateString
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkoutsFragment : Fragment() {
    private lateinit var preferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val navController = findNavController()


        return ComposeView(context = requireContext()).apply {
            setContent {
                TheNinjaMethodTheme {
                    ActivityContentScaffold(navController)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


}

@Composable
fun ActivityContentScaffold(navController: NavController) {
    val scrollstate = rememberScrollState()

    Scaffold(
        bodyContent = {
            ActivityBodyContent(
                modifier = Modifier.padding(it),
                navController = navController,
                scrollstate = scrollstate
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(R.id.navigate_to_start_workout) }) {
                Text(
                    text = "Start Workout"
                )
            }
//            StartWorkoutFab(
//                navController = navController,
//                extended = scrollstate.value == 0f,
//                modifier = Modifier
//            )
        }
    )
}

@Composable
fun StartWorkoutFab(
    navController: NavController,
    extended: Boolean,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = {
            navController.navigate(R.id.navigate_to_start_workout)
        },
        modifier = modifier
            .padding(16.dp)
            .preferredHeight(48.dp)
            .widthIn(min = 48.dp),
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary
    ) {
//        AnimatingFabContent(
//            icon = {
//                Image(
//                    imageVector = Icons.Default.Add,
//                    modifier = Modifier.preferredSize(24.dp)
//                )
//            },
//            text = {
//                Text(
//                    text = "Start Workout"
//                )
//            },
//            extended = extended
//        )
    }
}

@Composable
fun ActivityBodyContent(
    modifier: Modifier,
    navController: NavController,
    scrollstate: ScrollState
) {
    val viewModel = viewModel(WorkoutListViewModel::class.java)
    val workoutData by viewModel.workoutMapLiveData.observeAsState()
    val isLoggedIn by viewModel.isLoggedIn.observeAsState()

    if (workoutData.isNullOrEmpty()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    } else {
        ScrollableColumn(modifier = modifier, scrollState = scrollstate) {
            workoutData!!.forEach { dayWorkoutSummary ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                        .background(color = Color.Transparent)
                        .padding(start = 16.dp, bottom = 8.dp, end = 16.dp, top = 8.dp)
                        .clickable {
                            val bundle = bundleOf("workoutSummary" to dayWorkoutSummary)
                            navController.navigate(R.id.navigate_to_combined_workout, bundle)
                        }, elevation = 4.dp, border = BorderStroke(0.5.dp, Color.Gray)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = dayWorkoutSummary.first.getDateString(),
                            style = MaterialTheme.typography.h6
                        )

                        dayWorkoutSummary.second.forEach { workoutSummary ->
                            when (workoutSummary.workoutType) {
                                WorkoutType.LIFTING -> {
                                    WorkoutRow(
                                        navController = navController,
                                        navDestId = R.id.navigate_to_manual_workout_detail,
                                        workoutSummary = workoutSummary
                                    )
                                }
                                WorkoutType.STRAVA -> {
                                    WorkoutRow(
                                        navController = navController,
                                        navDestId = R.id.navigate_to_strava_workout_detail,
                                        workoutSummary = workoutSummary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun WorkoutRow(navController: NavController, navDestId: Int, workoutSummary: SimpleWorkout) {
    Column(modifier = Modifier.clickable {
        val bundle = bundleOf("workout" to workoutSummary)
        navController.navigate(
            navDestId,
            bundle
        )
    }) {
        Text(
            text = workoutSummary.workoutName,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(start = 10.dp, top = 10.dp)
        )
        Row(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 8.dp
            ).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val imageRes =
                if (workoutSummary.workoutType == WorkoutType.STRAVA) R.drawable.ic_distance
                else R.drawable.ic_weight

            WorkoutStat(
                imageRes = imageRes,
                value = workoutSummary.stravaDistance
            )

            WorkoutStat(
                imageRes = R.drawable.ic_clock,
                value = workoutSummary.stravaTime
            )

            if (workoutSummary.workoutCaloriesBurned.isNotEmpty()) {
                WorkoutStat(
                    imageRes = R.drawable.ic_fire,
                    value = workoutSummary.workoutCaloriesBurned
                )
            }
        }
    }
}

@Composable
fun WorkoutStat(imageRes: Int, value: String) {
    Row(horizontalArrangement = Arrangement.Center) {
        Image(
            bitmap = imageResource(imageRes),
            modifier = Modifier.preferredSize(20.dp)
        )
        Text(
            text = value,
            modifier = Modifier.padding(start = 5.dp)
        )
    }
}