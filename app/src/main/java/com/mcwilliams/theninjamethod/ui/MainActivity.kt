package com.mcwilliams.theninjamethod.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.strava.StravaLoginActivity
import com.mcwilliams.theninjamethod.strava.authorize.AccessScope
import com.mcwilliams.theninjamethod.strava.authorize.ApprovalPrompt
import com.mcwilliams.theninjamethod.strava.authorize.StravaLogin


class MainActivity : AppCompatActivity() {
    val RQ_LOGIN = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = this.findViewById(R.id.nav_view)

        val toolbar: Toolbar = this.findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

//        val intent = StravaLogin.withContext(this)
//            .withClientID(47849)
//            .withRedirectURI("https://www.supertech.ninja")
//            .withApprovalPrompt(ApprovalPrompt.AUTO)
//            .withAccessScope(AccessScope.VIEW_PRIVATE_WRITE)
//            .makeIntent();
//
//        startActivityForResult(intent, RQ_LOGIN);

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_workouts,
                R.id.navigation_exercises,
                R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RQ_LOGIN && resultCode == Activity.RESULT_OK && data != null) {
            val code = data.getStringExtra(StravaLoginActivity.RESULT_CODE)
            Log.d(TAG, "onActivityResult: $code")
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
