package com.mcwilliams.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
import androidx.core.util.Preconditions
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.mcwilliams.settings.model.ActivityTotal
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentView = inflater.inflate(R.layout.frame_layout, container, false)
        (fragmentView as ViewGroup).setContent(Recomposer.current()) {
            SettingsLayout(fragmentView, viewModel)
        }
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isLoggedIn.observe(viewLifecycleOwner, Observer {
            if (it) {
                viewModel.loadDetailedAthlete()
            }
        })

        parentFragmentManager.setFragmentResultListener(
            REQUEST_KEY,
            this,
            { requestKey, result ->
                onFragmentResult(requestKey, result)
            }
        )
    }

    @SuppressLint("RestrictedApi")
    private fun onFragmentResult(requestKey: String, result: Bundle) {
        Preconditions.checkState(REQUEST_KEY == requestKey)
        val authCode = result.getString("authCode")
        viewModel.loginAthlete(authCode!!)
    }

    companion object {
        private const val REQUEST_KEY = "authCode"
    }
}

@Composable
fun SettingsLayout(fragmentView: ViewGroup, viewModel: SettingsViewModel) {
    val isLoggedIn by viewModel.isLoggedIn.observeAsState()

    if (isLoggedIn!!) {

        val detailedAthlete by viewModel.detailedAthlete.observeAsState()
        detailedAthlete?.let {
            Column(modifier = Modifier.padding(16.dp).fillMaxHeight()) {
                Row() {
                    Card(
                        shape = CircleShape, modifier = Modifier.preferredWidth(100.dp)
                            .preferredHeight(100.dp)
                    ) {
//                        CoilImage(
//                            data = it.profile,
//                            contentScale = ContentScale.Crop,
//                            modifier = Modifier.preferredWidth(100.dp)
//                                .preferredHeight(100.dp),
//                            loading = {
//                                Box(
//                                    modifier = Modifier.fillMaxSize(),
//                                    gravity = ContentGravity.Center
//                                ) {
//                                    CircularProgressIndicator()
//                                }
//                            }
//                        )
                    }

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

                    var screenState by remember { mutableStateOf(0) }

                    Tabs(
                        selectedTab = screenState,
                        onSelected = { index ->
                            screenState = index
                        }
                    )

                    when (screenState) {
                        0 -> {
                            ShowStatsByType(
                                listOf(
                                    it.recent_ride_totals,
                                    it.ytd_ride_totals,
                                    it.all_ride_totals
                                )
                            )
                        }
                        1 -> {
                            ShowStatsByType(
                                listOf(
                                    it.recent_run_totals,
                                    it.ytd_run_totals,
                                    it.all_run_totals
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

                Button(content = {
                    Text("Log off")
                }, onClick = {
                    viewModel.logOff()
                })
            }
        }

    } else {
        Column {
            Button(content = {
                Text("Log into Strava")
            }, onClick = {
                Navigation.findNavController(fragmentView).navigate(R.id.navigate_to_strava_auth)
            })
        }
    }
}

@Composable
fun Tabs(
    selectedTab: Int,
    onSelected: (Int) -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.wrapContentHeight().padding(top = 8.dp),
    ) {
        TabRow(selectedTabIndex = selectedTab, backgroundColor = Color(0xFF059EDC)) {
            val tabTitles = listOf(
                "Ride", "Run", "Swim"
            )
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = selectedTab == index,
                    onClick = { onSelected(index) },
                    selectedContentColor = Color.Black,
                    unselectedContentColor = Color.LightGray
                )
            }
        }
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