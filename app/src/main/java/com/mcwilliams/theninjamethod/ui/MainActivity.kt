package com.mcwilliams.theninjamethod.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.mcwilliams.data.workoutdb.SimpleWorkout
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.theme.TheNinjaMethodTheme
import com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist.ActivityContentScaffold
import com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist.WorkoutListViewModel
import com.mcwilliams.theninjamethod.ui.activity.manualworkoutdetail.ManualWorkoutDetailContent
import com.mcwilliams.theninjamethod.ui.activity.manualworkoutdetail.ManualWorkoutViewModel
import com.mcwilliams.theninjamethod.ui.exercises.ExerciseList
import com.mcwilliams.theninjamethod.ui.exercises.viewmodel.ExerciseListViewModel
import com.mcwilliams.theninjamethod.ui.routines.RoutinesScaffold
import com.mcwilliams.theninjamethod.ui.routines.RoutinesViewModel
import com.mcwilliams.theninjamethod.ui.settings.SettingsLayout
import com.mcwilliams.theninjamethod.ui.settings.SettingsViewModel
import com.mcwilliams.theninjamethod.ui.startworkout.StartWorkoutFrame
import com.mcwilliams.theninjamethod.ui.startworkout.StartWorkoutViewModel
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            val items = listOf(
                NavigationDestination.CombinedWorkouts,
                NavigationDestination.Routines,
                NavigationDestination.Exercises,
                NavigationDestination.Settings,
            )
            TheNinjaMethodTheme() {
                Scaffold(
                    bottomBar = {
                        BottomNavigation {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)
                            items.forEach { screen ->
                                BottomNavigationItem(
                                    icon = {
                                        Icon(
                                            painterResource(id = screen.resId!!),
                                            contentDescription = "", modifier = Modifier.size(24.dp)
                                        )
                                    },
                                    label = { Text(screen.label!!) },
                                    selected = currentRoute == screen.destination,
                                    onClick = {
                                        navController.navigate(screen.destination) {
                                            // Pop up to the start destination of the graph to
                                            // avoid building up a large stack of destinations
                                            // on the back stack as users select items
                                            popUpTo = navController.graph.startDestination
                                            // Avoid multiple copies of the same destination when
                                            // reselecting the same item
                                            launchSingleTop = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { paddingValues ->
                    NavHost(
                        navController,
                        startDestination = NavigationDestination.CombinedWorkouts.destination
                    ) {
                        composable(NavigationDestination.CombinedWorkouts.destination) {
                            val viewModel: WorkoutListViewModel = hiltNavGraphViewModel()
                            ActivityContentScaffold(
                                navController = navController,
                                viewModel = viewModel,
                                paddingValues = paddingValues
                            )
                        }
                        composable(NavigationDestination.StartAWorkout.destination) {
                            val viewModel: StartWorkoutViewModel =
                                hiltNavGraphViewModel()
                            StartWorkoutFrame(
                                navController = navController,
                                startWorkoutViewModel = viewModel,
                                paddingValues = paddingValues
                            )
                        }
                        composable(NavigationDestination.Routines.destination) {
                            val viewModel: RoutinesViewModel =
                                hiltNavGraphViewModel()

                            RoutinesScaffold(
                                navController = navController,
                                routinesViewModel = viewModel,
                                paddingValues = paddingValues
                            )
                        }
                        composable(NavigationDestination.Exercises.destination) {
                            val viewModel: ExerciseListViewModel =
                                hiltNavGraphViewModel()

                            ExerciseList(viewModel = viewModel, paddingValues = paddingValues)
                        }

                        composable(NavigationDestination.Settings.destination) {
                            val viewModel: SettingsViewModel = hiltNavGraphViewModel()

                            SettingsLayout(
                                navController = navController,
                                viewModel = viewModel,
                                paddingValues = paddingValues
                            )
                        }

                        composable(
                            NavigationDestination.ManualWorkoutDetail.destination,
//                            arguments = listOf(navArgument("userId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val viewModel: ManualWorkoutViewModel = hiltNavGraphViewModel()

                            ManualWorkoutDetailContent(
                                navController = navController,
                                workoutId = backStackEntry.arguments?.getString("workoutId", "")!!,
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }
        }
    }
}

sealed class NavigationDestination(
    val destination: String,
//    val argKey: String? = null,
//    val route: String = destination + (argKey ?: ""),
    val label: String? = null,
    @DrawableRes val resId: Int? = null,
) {
    object CombinedWorkouts :
        NavigationDestination("combinedWorkouts", "Activity", resId = R.drawable.ic_running_icon)

    object Routines :
        NavigationDestination("routines", "Routines", resId = R.drawable.ic_routine_icon)

    object Exercises :
        NavigationDestination("exercises", "Exercises", resId = R.drawable.ic_exercises_icon)

    object Settings : NavigationDestination(
        "settings",
        "Settings",
        resId = R.drawable.ic_notifications_black_24dp
    )

    object StartAWorkout : NavigationDestination("startAWorkout")
    object ManualWorkoutDetail : NavigationDestination("manualWorkoutDetail/{workoutId}")
    object StravaWorkoutDetail : NavigationDestination("stravaWorkoutDetail")
    object CombinedWorkoutDetail : NavigationDestination("combinedWorkoutDetail")
    object ShareCombinedWorkout : NavigationDestination("shareCombinedWorkout")
    object ShareStravaWorkout : NavigationDestination("shareStravaWorkout")
    object ShareManualWorkout : NavigationDestination("shareManualWorkout")
}
