package com.mcwilliams.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Preconditions
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_settings.*

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.detailedAthlete.observe(viewLifecycleOwner, Observer { dathlete ->
            detailed_athlete.text = dathlete.toString()
            detailed_athlete.hideOtherViews()
        })

        viewModel.isLoggedIn.observe(viewLifecycleOwner, Observer {
            if (it) {
                btnLogin2Strava.visibility = View.GONE
                athleteName.visibility = View.GONE
                detailed_athlete.visibility = View.VISIBLE
                viewModel.loadDetailedAthlete()
            } else {
                btnLogin2Strava.visibility = View.VISIBLE
            }
        })

        button_log_off.setOnClickListener {
            viewModel.logOff()
        }

        parentFragmentManager.setFragmentResultListener(
            REQUEST_KEY,
            this,
            FragmentResultListener { requestKey, result ->
                onFragmentResult(requestKey, result)
            }
        )

        btnLogin2Strava.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.navigate_to_strava_auth)
        }

    }

    @SuppressLint("RestrictedApi")
    private fun onFragmentResult(requestKey: String, result: Bundle) {
        Preconditions.checkState(REQUEST_KEY == requestKey)
        val authCode = result.getString("authCode")
        viewModel.loginAthlete(authCode!!)
    }


    private fun View.hideOtherViews() {
        btnLogin2Strava.visibility = View.GONE
        athleteName.visibility = View.GONE
        detailed_athlete.visibility = View.GONE
        this.visibility = View.VISIBLE
    }

    companion object {
        private const val TAG = "SettingsFragment"
        private const val REQUEST_KEY = "authCode"
    }
}
