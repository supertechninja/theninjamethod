package com.mcwilliams.theninjamethod.ui.settings

import android.annotation.TargetApi
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import coil.api.load
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.compose.ActivityTotalCardModel
import com.mcwilliams.theninjamethod.compose.Profile
import com.mcwilliams.theninjamethod.strava.AccessScope
import com.mcwilliams.theninjamethod.strava.ApprovalPrompt
import com.mcwilliams.theninjamethod.strava.StravaLogin
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_settings.*

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModels()
    lateinit var loginWebview: WebView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    private fun setProfileVisibility(visibility: Int) {
        if (visibility != View.VISIBLE && visibility != View.GONE && visibility != View.INVISIBLE) return
        athleteName.visibility = visibility
        profile_picture.visibility = visibility
    }

    private fun setLoginVisibility(visibility: Int) {

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.detailedAthlete.observe(viewLifecycleOwner, Observer { stravaAthlete ->
            detailed_athlete.text = stravaAthlete.toString()
            profile_picture.load(stravaAthlete.profile_medium)
            athleteName.text = stravaAthlete.firstname + " " + stravaAthlete.lastname
//            setProfileVisibility(View.VISIBLE)
            btnLogin2Strava.visibility = View.GONE
            loginWebview.visibility = View.GONE
            stravaAthlete.apply {
                if (context != null) {
                    totals.addView(Profile(
                        context = context ?: requireContext(),
                        username = username,
                        name = "${firstname} ${lastname}",
                        premium = premium,
                        followerCount = follower_count,
                        friendCount = friend_count,
                        location = "${city}, ${state}, ${country}",
                        profilePic = profile_medium,
                    ))
                }
            }
        })

        viewModel.activityStats.observe(viewLifecycleOwner, Observer { stats ->
            detailed_athlete.visibility = View.GONE
            context?.let {
                totals.addView( ActivityTotalCardModel(context = it, activityTotal = stats.all_ride_totals) )
                totals.addView( ActivityTotalCardModel(context = it, activityTotal = stats.all_run_totals) )
                totals.addView( ActivityTotalCardModel(context = it, activityTotal = stats.all_swim_totals) )
                totals.addView( ActivityTotalCardModel(context = it, activityTotal = stats.recent_ride_totals) )
                totals.addView( ActivityTotalCardModel(context = it, activityTotal = stats.recent_run_totals) )
                totals.addView( ActivityTotalCardModel(context = it, activityTotal = stats.recent_swim_totals) )
                totals.addView( ActivityTotalCardModel(context = it, activityTotal = stats.ytd_ride_totals) )
                totals.addView( ActivityTotalCardModel(context = it, activityTotal = stats.ytd_run_totals) )
                totals.addView( ActivityTotalCardModel(context = it, activityTotal = stats.ytd_swim_totals) )
            }
        })

        viewModel.isLoggedIn.observe(viewLifecycleOwner, Observer {
            if (it) {
                btnLogin2Strava.visibility = View.GONE
                athleteName.visibility = View.GONE
                loginWebview.visibility = View.GONE
                detailed_athlete.visibility = View.VISIBLE
                viewModel.loadDetailedAthlete()
            } else {
                btnLogin2Strava.visibility = View.VISIBLE
            }
        })

        button_log_off.setOnClickListener {
            viewModel.logOff()
        }

        loginWebview = view.findViewById(R.id.login_webview) as WebView

        btnLogin2Strava.setOnClickListener {
            val loginUrl = loadLoginUrl()
            configureWebViewClient()
            loginWebview.visibility = View.VISIBLE
            loginWebview.loadUrl(loginUrl)
        }
    }

    //Called after permission granted in webview auth
    private fun handleStravaLogin(code: String) {
        viewModel.loginAthlete(code)
    }

    private fun View.hideOtherViews() {
        btnLogin2Strava.visibility = View.GONE
        athleteName.visibility = View.GONE
        detailed_athlete.visibility = View.GONE
        loginWebview.visibility = View.GONE
        this.visibility = View.VISIBLE
    }

    private fun loadLoginUrl(): String {
        return StravaLogin.withContext(activity)
            .withClientID(CLIENT_ID)
            .withRedirectURI(redirectUrl)
            .withApprovalPrompt(ApprovalPrompt.AUTO)
            .withAccessScope(AccessScope.VIEW_PRIVATE_WRITE)
            .makeLoginURL()
    }

    private fun configureWebViewClient() {
        loginWebview.settings.javaScriptEnabled = true
        loginWebview.settings.userAgentString = "Mozilla/5.0 Google"
        loginWebview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return handleUrl(Uri.parse(url)) || super.shouldOverrideUrlLoading(view, url)
            }

            @TargetApi(Build.VERSION_CODES.N)
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                val uri = request.url
                return handleUrl(uri) || super.shouldOverrideUrlLoading(view, request)
            }

            private fun handleUrl(uri: Uri): Boolean {
                //                val redirectURL: String =
                //                    getIntent().getStringExtra(StravaLoginActivity.EXTRA_REDIRECT_URL)
                if (uri.toString().startsWith(redirectUrl)) {
                    val code = uri.getQueryParameter("code")
                    return makeResult(code)
                }
                return false
            }

            private fun makeResult(code: String?): Boolean {
                if (code != null && code.isNotEmpty()) {
                    loginWebview.visibility = View.GONE
                    handleStravaLogin(code)
                    return true
                }
                //                finish()
                return false
            }
        }
    }

    companion object {
        private const val TAG = "SettingsFragment"
        const val redirectUrl = "https://www.supertech.ninja"
        private const val CLIENT_ID = 47849
    }
}
