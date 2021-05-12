package com.mcwilliams.theninjamethod.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.mcwilliams.theninjamethod.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            NavHost(navController, startDestination = NavigationDestination.CombinedWorkouts.destination) {
                composable(NavigationDestination.CombinedWorkouts.destination) {
                    val viewModel: Screen1ViewModel = hiltNavGraphViewModel()
                    Screen1(viewModel = viewModel)
                    ActivityContentScaffold()
                }
                composable(NavigationDestination.StartAWorkout.destination) {
                    val viewModel: Screen1ViewModel = hiltNavGraphViewModel()
                    Screen1(viewModel = viewModel)
                }
            }
        }

x
//        setContentView(R.layout.activity_main)
//        val navView: BottomNavigationView = this.findViewById(R.id.nav_view)
//
//        val toolbar: Toolbar = this.findViewById(R.id.toolbar)
//        setSupportActionBar(toolbar)
//
//        val navController = findNavController(R.id.nav_host_fragment)
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_workouts,
//                R.id.navigation_routines,
//                R.id.navigation_exercises,
//                R.id.navigation_settings
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
//        navView.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
//
//        navController.addOnDestinationChangedListener { controller, destination, arguments ->
//            // react on change
//            // you can check destination.id or destination.label and act based on that
//            when (destination.id) {
//                R.id.navigation_start_workout -> {
//                    toolbar.visibility = View.GONE
//                    navView.visibility = View.GONE
//                }
//                else -> {
//                    navView.visibility = View.VISIBLE
//                    toolbar.visibility = View.VISIBLE
//                }
//            }
//        }
    }
}

sealed class NavigationDestination(val destination: String) {
    object CombinedWorkouts : NavigationDestination("combinedWorkouts")
    object StartAWorkout : NavigationDestination("startAWorkout")
    object Routines : NavigationDestination("routines")
    object ManualWorkoutDetail : NavigationDestination("manualWorkoutDetail")
    object StravaWorkoutDetail : NavigationDestination("stravaWorkoutDetail")
    object CombinedWorkoutDetail : NavigationDestination("combinedWorkoutDetail")
    object ShareCombinedWorkout : NavigationDestination("shareCombinedWorkout")
    object ShareStravaWorkout : NavigationDestination("shareStravaWorkout")
    object ShareManualWorkout : NavigationDestination("shareManualWorkout")
    object Exercises : NavigationDestination("exercises")
    object Settings : NavigationDestination("settings")
}
