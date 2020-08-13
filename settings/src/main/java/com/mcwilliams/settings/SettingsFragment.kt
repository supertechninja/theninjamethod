package com.mcwilliams.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Box
import androidx.compose.foundation.ContentGravity
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
import androidx.core.util.Preconditions
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.accompanist.coil.CoilImage
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
        viewModel.loadDetailedAthlete()

        val detailedAthlete by viewModel.detailedAthlete.observeAsState()
        detailedAthlete?.let {
            Column(modifier = Modifier.padding(16.dp).fillMaxHeight()) {
                Row() {
                    Card(
                        shape = CircleShape, modifier = Modifier.preferredWidth(100.dp)
                            .preferredHeight(100.dp)
                    ) {
                        CoilImage(
                            data = it.profile,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.preferredWidth(100.dp)
                                .preferredHeight(100.dp),
                            loading = {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    gravity = ContentGravity.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        )
                    }

                    Text(
                        text = "${it.firstname} ${it.lastname}",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(start = 16.dp),
                        color = Color.White
                    )
                }

                val athleteStats by viewModel.athleteStats.observeAsState()
                athleteStats?.let {
                    it.forEach { athleteStat ->
                        Text(
                            athleteStat.first,
                            modifier = Modifier.padding(vertical = 8.dp),
                            style = MaterialTheme.typography.h6,
                            color = Color.White
                        )

                        athleteStat.second.forEachIndexed { index, activityTotal ->
                            when (index) {
                                0 -> {
                                    Text(
                                        "Recent: ${(activityTotal?.distance?.div(1609))?.roundToInt()} miles",
                                        color = Color.White,
                                        style = MaterialTheme.typography.body1
                                    )
                                }
                                1 -> {
                                    Text(
                                        "Year to Date: ${(activityTotal?.distance?.div(1609))?.roundToInt()} miles",
                                        color = Color.White,
                                        style = MaterialTheme.typography.body1
                                    )
                                }
                                2 -> {
                                    Text(
                                        "All-time: ${(activityTotal?.distance?.div(1609))?.roundToInt()} miles",
                                        color = Color.White,
                                        style = MaterialTheme.typography.body1
                                    )

                                }
                            }
                        }
                    }

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