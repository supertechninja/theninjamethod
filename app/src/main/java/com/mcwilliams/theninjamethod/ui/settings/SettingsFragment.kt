package com.mcwilliams.theninjamethod.ui.settings

import android.annotation.TargetApi
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import coil.api.load
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.strava.AccessScope
import com.mcwilliams.theninjamethod.strava.ApprovalPrompt
import com.mcwilliams.theninjamethod.strava.StravaLogin
import com.mcwilliams.theninjamethod.strava.model.athlete.ActivityTotal
import com.mcwilliams.theninjamethod.ui.settings.data.Athlete
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.athleteName
import kotlinx.android.synthetic.main.fragment_settings.btnLogin2Strava
import kotlinx.android.synthetic.main.fragment_settings.view.*

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.detailedAthlete.observe(viewLifecycleOwner, Observer { dathlete ->
            profile_zone.visibility = View.VISIBLE
            profile_picture.load(dathlete.profile_medium)
            athleteName.text = "${dathlete.firstname} ${dathlete.lastname}"
            username.text = dathlete.username
            location.text = "${dathlete.city}, ${dathlete.state}, ${dathlete.country}"
        })

        viewModel.activityStats.observe(viewLifecycleOwner, Observer { stats ->
            totals_sv.visibility = View.VISIBLE
            addTotalView("All Ride Totals", stats.all_ride_totals)
            addTotalView("All Run Totals", stats.all_run_totals)
            addTotalView("All Swim Totals", stats.all_swim_totals)
            addTotalView("Recent Ride Totals", stats.recent_ride_totals)
            addTotalView("Recent Run Totals", stats.recent_run_totals)
            addTotalView("Recent Swim Totals", stats.recent_swim_totals)
            addTotalView("YTD Ride Totals", stats.ytd_ride_totals)
            addTotalView("YTD Run Totals", stats.ytd_run_totals)
            addTotalView("YTD Swim Totals", stats.ytd_swim_totals)
        })

        viewModel.isLoggedIn.observe(viewLifecycleOwner, Observer {
            if (it) {
                btnLogin2Strava.visibility = View.GONE
                loginWebview.visibility = View.GONE
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

    private fun addTotalView(title: String, at: ActivityTotal) {
        context?.let {
            totals.addView(ActivityTotalCard(it,  title, at, null, 0))
        }
    }

    //Called after permission granted in webview auth
    private fun handleStravaLogin(code: String) {
        viewModel.loginAthlete(code)
    }

    private fun View.hideOtherViews() {
        btnLogin2Strava.visibility = View.GONE
        profile_zone.visibility = View.GONE
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
