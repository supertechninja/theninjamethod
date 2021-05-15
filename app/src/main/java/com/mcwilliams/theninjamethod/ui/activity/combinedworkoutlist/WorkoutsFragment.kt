package com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.core.os.bundleOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.mcwilliams.data.workoutdb.SimpleWorkout
import com.mcwilliams.data.workoutdb.WorkoutType
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.ui.NavigationDestination
import com.mcwilliams.theninjamethod.utils.extensions.getDateString

@ExperimentalFoundationApi
@Composable
fun ActivityContentScaffold(
    viewModel: WorkoutListViewModel,
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    val scrollstate = rememberScrollState()

    Scaffold(
        content = {
            ActivityBodyContent(
                navController = navController,
                scrollstate = scrollstate,
                viewModel = viewModel
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(NavigationDestination.StartAWorkout.destination) },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary
            ) {
                Text(
                    text = "Start Workout",
                    modifier = Modifier.padding(16.dp)
                )
            }
        },
        modifier = Modifier.padding(paddingValues = paddingValues)
    )
}

@ExperimentalFoundationApi
@Composable
fun ActivityBodyContent(
    navController: NavHostController,
    scrollstate: ScrollState,
    viewModel: WorkoutListViewModel
) {

    val workoutData by viewModel.workoutMapLiveData.observeAsState()
    val isLoggedIn by viewModel.isLoggedIn.observeAsState()

    Column(modifier = Modifier.fillMaxSize()) {

        Row(
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "All Workouts",
                style = MaterialTheme.typography.h4,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(start = 16.dp)
            )

            if (isLoggedIn!!) {
                Image(
                    painter = painterResource(id = R.drawable.ic_profile),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(40.dp)
                        .clickable {
                            navController.navigate(NavigationDestination.Settings.destination)
                        }
                )
            }
        }

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
                workoutData!!.fastForEachIndexed { index, (date, workouts) ->
                    stickyHeader {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val bundle = bundleOf("workoutSummary" to workoutData!![index])
                                    navController.navigate(
                                        R.id.navigate_to_combined_workout,
                                        bundle
                                    )
                                }
                                .background(Color(0x80444444))
                        ) {
                            Text(
                                text = date.getDateString(),
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .padding(vertical = 4.dp),
                                style = MaterialTheme.typography.h6
                            )
                        }
                    }

                    items(workouts) {
                        when (it.workoutType) {
                            WorkoutType.LIFTING -> {
                                WorkoutRow(
                                    navController = navController,
                                    openDetail = {
                                        navController.navigate("manualWorkoutDetail/${it.id}")
                                    },
                                    workoutSummary = it
                                )
                            }
                            WorkoutType.STRAVA -> {
                                WorkoutRow(
                                    navController = navController,
                                    openDetail = {
                                        navController.navigate("stravaWorkoutDetail/${it.id}")
                                    },
                                    workoutSummary = it
                                )
                            }
                        }
                    }
                }
            })
        }
    }
}

@Composable
fun WorkoutRow(
    navController: NavHostController,
    openDetail: () -> Unit,
    workoutSummary: SimpleWorkout
) {
    Column(modifier = Modifier.clickable {
        openDetail()
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
    modifier: Modifier = Modifier
) {
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.padding(vertical = 2.dp)) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = "",
            modifier = modifier,
            colorFilter = ColorFilter.tint(color = MaterialTheme.colors.onSurface)
        )
        Text(
            text = value,
            modifier = Modifier
                .padding(start = 5.dp)
                .align(Alignment.CenterVertically),
            style = MaterialTheme.typography.body1,
        )
    }
}