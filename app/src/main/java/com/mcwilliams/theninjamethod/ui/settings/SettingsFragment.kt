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
import com.mcwilliams.theninjamethod.utils.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject

class SettingsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: SettingsViewModel by viewModels { viewModelFactory }

    lateinit var loginWebview: WebView
    lateinit var loginUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginWebview = view.findViewById(R.id.login_webview) as WebView

        loginUrl = StravaLogin.withContext(activity)
            .withClientID(47849)
            .withRedirectURI("https://www.supertech.ninja")
            .withApprovalPrompt(ApprovalPrompt.AUTO)
            .withAccessScope(AccessScope.VIEW_PRIVATE_WRITE)
            .makeLoginURL()

        configureWebViewClient()

        btnLogin2Strava.setOnClickListener{
            loginWebview.visibility = View.VISIBLE
            loginWebview.loadUrl(loginUrl)
        }
//        loginWebview.loadUrl(loginUrl)

    }

    private fun initViews(code: String) {
        viewModel.loginAthlete(code)
    }

    private fun initObservers() {
        viewModel.resultLogin.observe(this, Observer { athlete ->
            athlete?.let {
                Log.d(TAG, "initObservers: " + athlete.firstname)
                loadAthlete(it)
//                showData(it)
//                binding.fabShare.setOnClickListener { shareWorldStats() }
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

    private fun loadAthlete(athlete: Athlete){
        btnLogin2Strava.visibility = View.GONE
        athleteName.text = "${athlete.firstname} ${athlete.lastname}"
    }

    private fun configureWebViewClient() {
        loginWebview.getSettings().setJavaScriptEnabled(true)
        loginWebview.getSettings().setUserAgentString("Mozilla/5.0 Google")
        loginWebview.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return handleUrl(Uri.parse(url)) || super.shouldOverrideUrlLoading(
                    view,
                    url
                )
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
                if (code != null && !code.isEmpty()) {
                    loginWebview.visibility = View.GONE
                    initViews(code)
                    initObservers()
                    return true
                }
//                finish()
                return false
            }
        })
    }

    companion object {
        private const val TAG = "SettingsFragment"
        const val redirectUrl = "https://www.supertech.ninja"
    }
}
