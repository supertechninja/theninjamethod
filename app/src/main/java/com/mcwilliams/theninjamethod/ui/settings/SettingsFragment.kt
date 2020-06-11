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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.strava.AccessScope
import com.mcwilliams.theninjamethod.strava.ApprovalPrompt
import com.mcwilliams.theninjamethod.strava.StravaLogin
import com.mcwilliams.theninjamethod.ui.settings.data.Athlete
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isLoggedIn.observe(viewLifecycleOwner, Observer {
            if (it) {
                btnLogin2Strava.visibility = View.GONE
                athleteName.visibility = View.GONE
                loginWebview.visibility = View.GONE
                detailed_athlete.visibility = View.VISIBLE
                getDetailedAthelete()
            }
        })

        loginWebview = view.findViewById(R.id.login_webview) as WebView

        btnLogin2Strava.setOnClickListener {
            val loginUrl = loadLoginUrl()
            configureWebViewClient()
            loginWebview.visibility = View.VISIBLE
            loginWebview.loadUrl(loginUrl)
        }

    }

    private fun initViews(code: String) {
        viewModel.loginAthlete(code)
    }

    private fun initObservers() {
        viewModel.resultLogin.observe(this, Observer { athlete ->
            athlete?.let {
                Log.d(TAG, "initObservers: " + athlete.firstname)
                button_get_athlete.hideOtherViews()
                button_get_athlete.setOnClickListener(View.OnClickListener {
                    getDetailedAthelete()
                })
//                showData(it)
            } ?: kotlin.run {
//                handleError()
            }
        })

//        getViewModel().errorMessage.observe(this, Observer {
//            handleError()
//        })
//        getViewModel().showLoading.observe(this, Observer { showLoading ->
//            if (showLoading) binding.progress.show()
//            else binding.progress.hide()
//        })
    }

    private fun getDetailedAthelete() {
        viewModel.loadDetailedAthlete()
        viewModel.detailedAthlete.observe(viewLifecycleOwner, Observer { dathlete ->
            detailed_athlete.text = dathlete.toString()
            detailed_athlete.hideOtherViews()
        })
    }

    private fun View.hideOtherViews() {
        btnLogin2Strava.visibility = View.GONE
        athleteName.visibility = View.GONE
        detailed_athlete.visibility = View.GONE
        loginWebview.visibility = View.GONE
        this.visibility = View.VISIBLE
    }

    private fun loadAthlete(athlete: Athlete) {
        btnLogin2Strava.visibility = View.GONE
        athleteName.text = "${athlete.firstname} ${athlete.lastname}"
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
                    initViews(code)
                    initObservers()
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
