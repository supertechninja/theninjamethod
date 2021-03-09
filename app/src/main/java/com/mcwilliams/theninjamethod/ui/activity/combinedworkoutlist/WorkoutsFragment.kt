package com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.composethemeadapter.MdcTheme
import com.mcwilliams.data.workoutdb.SimpleWorkout
import com.mcwilliams.data.workoutdb.WorkoutType
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.utils.extensions.getDateString
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import kotlin.random.Random

@AndroidEntryPoint
class WorkoutsFragment : Fragment() {
    private lateinit var preferences: SharedPreferences
    private val viewModel: WorkoutListViewModel by viewModels()

    @ExperimentalFoundationApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val navController = findNavController()


        return ComposeView(context = requireContext()).apply {
            setContent {
                MdcTheme {
                    ActivityContentScaffold(navController, viewModel)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


}

@ExperimentalFoundationApi
@Composable
fun ActivityContentScaffold(navController: NavController, viewModel: WorkoutListViewModel) {
    val scrollstate = rememberScrollState()

    Scaffold(
        content = {
            ActivityBodyContent(
                modifier = Modifier.padding(it),
                navController = navController,
                scrollstate = scrollstate,
                viewModel = viewModel
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(R.id.navigate_to_start_workout) }) {
                Text(
                    text = "Start Workout",
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
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
            .requiredHeight(48.dp)
            .widthIn(min = 48.dp),
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary
    ) {
    }
}

@ExperimentalFoundationApi
@Composable
fun ActivityBodyContent(
    modifier: Modifier,
    navController: NavController,
    scrollstate: ScrollState,
    viewModel: WorkoutListViewModel
) {

    val workoutData by viewModel.workoutMapLiveData.observeAsState()
    val isLoggedIn by viewModel.isLoggedIn.observeAsState()
    var isGridView by remember { mutableStateOf(true) }

    if (workoutData.isNullOrEmpty()) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(100.dp)
            )
        }
    } else {
        LazyColumn(content = {
            workoutData!!.forEach { (date, workouts) ->
                stickyHeader {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0x80444444))) {
                        Text(
                            text = date.getDateString(),
                            modifier = Modifier.padding(start = 16.dp),
                            style = MaterialTheme.typography.h6
                        )
                    }
                }

                items(workouts) {
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .background(color = Color.Transparent)
//                            .padding(start = 16.dp, bottom = 8.dp, end = 16.dp, top = 8.dp)
//                            .clickable {
////                                val bundle = bundleOf("workoutSummary" to dayWorkoutSummary)
////                                navController.navigate(R.id.navigate_to_combined_workout, bundle)
//                            }, elevation = 4.dp, border = BorderStroke(0.5.dp, Color.Gray)
//                    ) {
//                        Column(modifier = Modifier.padding(10.dp)) {
//                            workouts.forEach { workoutSummary ->
                    when (it.workoutType) {
                        WorkoutType.LIFTING -> {
                            WorkoutRow(
                                navController = navController,
                                navDestId = R.id.navigate_to_manual_workout_detail,
                                workoutSummary = it
                            )
                        }
                        WorkoutType.STRAVA -> {
                            WorkoutRow(
                                navController = navController,
                                navDestId = R.id.navigate_to_strava_workout_detail,
                                workoutSummary = it
                            )
                        }
                    }
                }
//                        }
//                    }
            }
//            }
        })
    }
}


@Composable
fun WorkoutWithRandomHeight(
    workout: SimpleWorkout,
    first: LocalDate,
    navController: NavController
) {
    // Randomly pick height for album but remember the same height for that album.
    val randomHeight = remember(workout.id) { Random.nextInt(150, 250).dp }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            elevation = 12.dp,
            modifier = Modifier
                .padding(6.dp)
                .clickable {
                    val bundle = bundleOf("workout" to workout)
                    when (workout.workoutType) {
                        WorkoutType.LIFTING -> {
                            navController.navigate(R.id.navigate_to_manual_workout_detail, bundle)
                        }
                        WorkoutType.STRAVA -> {
                            navController.navigate(R.id.navigate_to_strava_workout_detail, bundle)
                        }
                    }


                }
        ) {
            Column(
                modifier = Modifier.height(randomHeight),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = workout.workoutName,
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.h6.copy(fontSize = 16.sp),
                    maxLines = 2
                )

                Column(
                    modifier = Modifier
                        .padding(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        )
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = first.getDateString(),
                        modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.h6.copy(fontSize = 14.sp)
                    )

                    val imageRes =
                        if (workout.workoutType == WorkoutType.STRAVA) R.drawable.ic_distance
                        else R.drawable.ic_weight

                    WorkoutStat(
                        imageRes = imageRes,
                        value = workout.stravaDistance,
                        imageModifier = Modifier.requiredHeight(30.dp)
                    )

                    WorkoutStat(
                        imageRes = R.drawable.ic_clock,
                        value = workout.stravaTime,
                        imageModifier = Modifier.requiredHeight(30.dp)
                    )

                    if (workout.workoutCaloriesBurned.isNotEmpty()) {
                        WorkoutStat(
                            imageRes = R.drawable.ic_fire,
                            value = workout.workoutCaloriesBurned
                        )
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
            modifier = Modifier.padding(start = 32.dp, top = 10.dp)
        )
        Row(
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val imageRes =
                if (workoutSummary.workoutType == WorkoutType.STRAVA) R.drawable.ic_distance
                else R.drawable.ic_weight

            WorkoutStat(
                imageRes = imageRes,
                value = workoutSummary.stravaDistance,
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
fun WorkoutStat(
    imageRes: Int,
    value: String,
    imageModifier: Modifier = Modifier.requiredHeight(20.dp)
) {
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.padding(vertical = 2.dp)) {
//        Image(
//            imageVector = loadImage(imageRes),
//            contentDescription = "",
//            modifier = imageModifier
//        )
        Text(
            text = value,
            modifier = Modifier
                .padding(start = 5.dp)
                .align(Alignment.CenterVertically),
            style = MaterialTheme.typography.body1,
        )
    }
}