package com.mcwilliams.theninjamethod.ui.routines

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.mcwilliams.data.workoutdb.Workout
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.theme.TheNinjaMethodTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoutinesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(context = requireContext()).apply {
            setContent {
                TheNinjaMethodTheme {
                    RoutinesScaffold(findNavController())
                }
            }
        }
    }
}

@Composable
fun RoutinesScaffold(navController: NavController) {
    val routinesViewModel = viewModel(RoutinesViewModel::class.java)
    Scaffold(
        bodyContent = {
            RoutinesBodyContent(
                modifier = Modifier.padding(it),
                navController = navController,
                routinesViewModel = routinesViewModel
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {}, icon = { Image(Icons.Default.Add) })
        }
    )

}

@Composable
fun RoutinesBodyContent(
    navController: NavController,
    routinesViewModel: RoutinesViewModel,
    modifier: Modifier
) {
    val routines by routinesViewModel.workout.observeAsState()
    var showRoutinesSummaryDialog by remember { mutableStateOf(false) }
    var routineToShow by remember { mutableStateOf(Workout(0, "", "")) }

    if (routines.isNullOrEmpty()) {
        Text(text = "No Routines Created")
    } else {
        LazyColumnFor(items = routines!!, modifier = modifier.fillMaxWidth().padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                        .clickable(onClick = {
                            showRoutinesSummaryDialog = true
                            routineToShow = it
                        })
                        .background(color = Color.Transparent)
                        .padding(start = 8.dp, bottom = 8.dp, end = 8.dp, top = 8.dp),
                    elevation = 4.dp,
                    border = BorderStroke(0.5.dp, Color.Gray)
                ) {
                    ConstraintLayout(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                        val (workoutName, startWorkoutButton) = createRefs()

                        Text(text = it.workoutName, modifier = Modifier.constrainAs(workoutName) {
                            start.linkTo(parent.start)
                            top.linkTo(startWorkoutButton.top)
                            bottom.linkTo(startWorkoutButton.bottom)
//                            end.linkTo(startWorkoutButton.start)
                        }, style = MaterialTheme.typography.h6, textAlign = TextAlign.Left)

                        OutlinedButton(onClick = {
                            val bundle = bundleOf("workout" to it)
                            Log.d("TAG", "RoutinesBodyContent: $bundle")
                            navController.navigate(
                                R.id.navigate_from_routines_to_start_workout,
                                bundle
                            )
                        }, modifier = Modifier.constrainAs(startWorkoutButton) {
                            end.linkTo(parent.end)
                        }, border = BorderStroke(0.5.dp, Color.Gray)) {
                            Text(
                                text = "Start".toUpperCase(),
                                color = MaterialTheme.colors.secondary,
                                modifier = Modifier.padding(horizontal = 8.dp),
                                style = MaterialTheme.typography.button
                            )
                        }
                    }
                }
            }
        }
    }

    if (showRoutinesSummaryDialog) {
        AlertDialog(
            onDismissRequest = {
                showRoutinesSummaryDialog = false
            },
            title = {
                Text("Routine Details", modifier = Modifier.padding(start = 8.dp, top = 8.dp))
            },
            buttons = {
                Column(horizontalGravity = Alignment.End) {
                    TextButton(onClick = {
                        showRoutinesSummaryDialog = false
                    }, modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)) {
                        Text(text = "OK")
                    }
                }
            },
            text = {
                routineToShow.exercises.forEach {
                    Text(text = it.exerciseName, modifier = Modifier.padding(4.dp))
                }
            })
    }
}