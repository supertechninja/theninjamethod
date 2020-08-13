package com.mcwilliams.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
import androidx.core.util.Preconditions
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint

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
            Column {
                Text(
                    text = "${detailedAthlete!!.firstname} ${detailedAthlete!!.lastname}",
                    color = Color.White
                )

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
