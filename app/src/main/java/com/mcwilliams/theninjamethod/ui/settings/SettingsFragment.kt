package com.mcwilliams.theninjamethod.ui.settings

import android.annotation.TargetApi
import android.content.Context
import android.net.Uri
import android.os.Build
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.google.accompanist.coil.rememberCoilPainter
import com.mcwilliams.appinf.model.StravaLogin
import com.mcwilliams.settings.model.ActivityTotal
import kotlin.math.roundToInt

@Composable
fun SettingsLayout(
    navController: NavController,
    viewModel: SettingsViewModel,
    paddingValues: PaddingValues
) {
    val isLoggedIn by viewModel.isLoggedIn.observeAsState()
    val scrollState = rememberScrollState()
    var selectedTab by remember { mutableStateOf(0) }
    var showLoginDialog by remember { mutableStateOf(false) }

    if (isLoggedIn!!) {
        viewModel.loadDetailedAthlete()
        Column(modifier = Modifier.padding(paddingValues = paddingValues)) {
            TabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier.height(56.dp),
                backgroundColor = MaterialTheme.colors.onPrimary,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = MaterialTheme.colors.onSurface,
                        height = 3.dp
                    )
                }
            ) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) {
                    Text(text = "Workouts", color = MaterialTheme.colors.onSurface)
                }

                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) {
                    Text(text = "Strava", color = MaterialTheme.colors.onSurface)
                }
            }


            val detailedAthlete by viewModel.detailedAthlete.observeAsState()
            detailedAthlete?.let {
                if (selectedTab == 1) {
                    Column(
                        modifier = Modifier
                            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                            .fillMaxHeight()
                            .verticalScroll(scrollState)
                    ) {

                        Row() {
                            Image(
                                painter = rememberCoilPainter(request = it.profile),
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                                    .border(1.dp, Color.White, shape = CircleShape),
                                contentDescription = "Profile Pic"
                            )

                            Text(
                                text = "${it.firstname} ${it.lastname}",
                                style = MaterialTheme.typography.h6,
                                modifier = Modifier.padding(start = 16.dp),
                                color = Color.White
                            )
                        }

                        Text(
                            text = "Strava Stats",
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                            color = Color.White
                        )

                        val athleteStats by viewModel.athleteStats.observeAsState()
                        athleteStats?.let {

                            var stravaTab by remember { mutableStateOf(0) }

                            TabRow(
                                selectedTabIndex = stravaTab,
                                modifier = Modifier.height(56.dp),
                                backgroundColor = MaterialTheme.colors.onPrimary,
                                indicator = { tabPositions ->
                                    TabRowDefaults.Indicator(
                                        modifier = Modifier.tabIndicatorOffset(tabPositions[stravaTab]),
                                        color = MaterialTheme.colors.onSurface,
                                        height = 3.dp
                                    )
                                }
                            ) {
                                Tab(selected = stravaTab == 0, onClick = { stravaTab = 0 }) {
                                    Text(text = "Run", color = MaterialTheme.colors.onSurface)
                                }
                                Tab(selected = stravaTab == 1, onClick = { stravaTab = 1 }) {
                                    Text(text = "Ride", color = MaterialTheme.colors.onSurface)
                                }
                                Tab(selected = stravaTab == 2, onClick = { stravaTab = 2 }) {
                                    Text(text = "Swim", color = MaterialTheme.colors.onSurface)
                                }

                            }

                            when (stravaTab) {
                                0 -> {
                                    ShowStatsByType(
                                        listOf(
                                            it.recent_run_totals,
                                            it.ytd_run_totals,
                                            it.all_run_totals
                                        )
                                    )

                                }
                                1 -> {
                                    ShowStatsByType(
                                        listOf(
                                            it.recent_ride_totals,
                                            it.ytd_ride_totals,
                                            it.all_ride_totals
                                        )
                                    )
                                }
                                2 -> {
                                    ShowStatsByType(
                                        listOf(
                                            it.recent_swim_totals,
                                            it.ytd_swim_totals,
                                            it.all_swim_totals
                                        )
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.requiredHeight(20.dp))
                        Button(content = {
                            Text("Log off")
                        }, onClick = {
                            viewModel.logOff()
                        })
                    }
                } else if (selectedTab == 0) {
                    WorkoutStats(viewModel = viewModel)
                }
            }
        }

    } else {
        Column(
            modifier = Modifier
                .padding(paddingValues = paddingValues)
                .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                .fillMaxHeight()
        ) {
            WorkoutStats(viewModel = viewModel)

            Button(content = {
                Text("Log into Strava")
            }, onClick = {
                showLoginDialog = !showLoginDialog
            })
        }
    }

    if (showLoginDialog) {
        val onFinish = { showLoginDialog = !showLoginDialog}
        Dialog(onDismissRequest = { showLoginDialog = !showLoginDialog }) {
            StravaAuthWebView(viewModel = viewModel, onFinish = onFinish)
        }
    }
}

@Composable
fun WorkoutStats(viewModel: SettingsViewModel) {
    val workoutsCount by viewModel.workoutHistory.observeAsState()
    workoutsCount?.let {
        Text(
            text = "Workout Stats",
            color = Color.White,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
        )
        Text(text = "Total Workouts Tracked: $it", color = Color.White)
    }
}

@Composable
fun ShowStatsByType(activites: List<ActivityTotal>) {
    Column {
        val weekly = activites[0]
        val yealy = activites[1]
        val alltime = activites[2]

        formattedHeaderText("AVG WEEKLY ACTIVITY")
        formattedText("Runs: ${weekly.count}")
        formattedText("Time: ${weekly.moving_time.getTimeString()}")
        formattedText("Distance: ${weekly.distance.div(1609).roundToInt()} mi")

        formattedHeaderText("YEAR TO DATE")
        formattedText("Runs: ${yealy.count}")
        formattedText("Time: ${yealy.moving_time.getTimeString()}")
        formattedText("Distance: ${yealy.distance.div(1609).roundToInt()} mi")

        formattedHeaderText("ALL TIME")
        formattedText("Runs: ${alltime.count}")
        formattedText("Distance: ${alltime.distance.div(1609).roundToInt()} mi")
    }
}

@Composable
fun formattedText(text: String) {
    Text(
        text, modifier = Modifier.padding(top = 2.dp, bottom = 2.dp),
        color = Color.White,
        style = MaterialTheme.typography.body2
    )
}

@Composable
fun formattedHeaderText(text: String) {
    Text(
        text, modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
        color = Color.White,
        style = MaterialTheme.typography.body1
    )
}


//Returns time based on seconds passed in
fun Int.getTimeString(): String {
    return "${(this / 60)}:${(this % 60)}"
}