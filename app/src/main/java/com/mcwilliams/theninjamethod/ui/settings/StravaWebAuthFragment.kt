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
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.strava.AccessScope
import com.mcwilliams.theninjamethod.strava.ApprovalPrompt
import com.mcwilliams.theninjamethod.strava.StravaLogin

class StravaWebAuthFragment : Fragment() {

    lateinit var loginWebview: WebView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.strava_auth_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginWebview = view.findViewById(R.id.login_webview) as WebView

        configureWebViewClient()
        loginWebview.loadUrl(loadLoginUrl())
    }

    private fun loadLoginUrl(): String = StravaLogin.withContext(activity)
        .withClientID(CLIENT_ID)
        .withRedirectURI(redirectUrl)
        .withApprovalPrompt(ApprovalPrompt.AUTO)
        .withAccessScope(AccessScope.VIEW_PRIVATE_WRITE)
        .makeLoginURL()


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
                if (uri.toString().startsWith(redirectUrl)) {
                    val code = uri.getQueryParameter("code")
                    return makeResult(code)
                }
                return false
            }

            private fun makeResult(code: String?): Boolean {
                if (code != null && code.isNotEmpty()) {
                    loginWebview.visibility = View.GONE

                    handleSuccessfulLogin(code)
                    return true
                }
                return false
            }
        }
    }

    fun handleSuccessfulLogin(code: String) {
        parentFragmentManager.setFragmentResult(
            "authCode", // Same request key FragmentA used to register its listener
            bundleOf("authCode" to code) // The data to be passed to FragmentA
        )
        parentFragmentManager.popBackStack()
    }


    companion object {
        const val redirectUrl = "https://www.supertech.ninja"
        private const val CLIENT_ID = 47849
    }
}