package com.mcwilliams.theninjamethod.ui.startworkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material.Text
import androidx.compose.material.Icon
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.WithConstraints
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mcwilliams.data.exercisedb.model.WorkoutSet
import com.mcwilliams.data.workoutdb.Workout
import com.mcwilliams.theninjamethod.theme.TheNinjaMethodTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@ExperimentalFoundationApi
@AndroidEntryPoint
class StartWorkoutFragment : Fragment() {

    var workout: Workout? = null
    private val startWorkoutViewModel: StartWorkoutViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            workout =
                arguments?.getSerializable("workout") as Workout
        }

        //Reset the previously saved workout
        startWorkoutViewModel._didSaveWorkout.postValue(false)

        setHasOptionsMenu(true)
        return ComposeView(requireContext()).apply {
            setContent {
                TheNinjaMethodTheme {
                    StartWorkoutFrame(
                        navController = findNavController(),
                        parentFragmentManager,
                        startWorkoutViewModel
                    )
                }
            }
        }
    }
}

fun getTimeOfDay(): String {
    val c: Calendar = Calendar.getInstance()
    return when (c.get(Calendar.HOUR_OF_DAY)) {
        in 0..11 -> {
            return "Morning Workout"
        }
        in 12..15 -> {
            return "Afternoon Workout"
        }
        in 16..20 -> {
            return "Evening Workout"
        }
        in 21..23 -> {
            return "Nighttime Workout"
        }
        else -> "Workout Name"
    }
}

@ExperimentalFoundationApi
@Composable
fun StartWorkoutFrame(
    navController: NavController,
    parentFragmentManager: FragmentManager,
    startWorkoutViewModel: StartWorkoutViewModel
) {
//    val startWorkoutViewModel = viewModel(StartWorkoutViewModel::class.java)
    val workout by startWorkoutViewModel.workout.observeAsState()

    val timeOfDay = getTimeOfDay()
    startWorkoutViewModel.updateWorkoutName(timeOfDay)

    var workoutDuration by remember { mutableStateOf("0:00") }

    val didSaveWorkout by startWorkoutViewModel.didSaveWorkout.observeAsState()
    if (didSaveWorkout!!) {
        navController.popBackStack()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = "Start Workout")
            }, actions = {
                IconButton(onClick = {
                    startWorkoutViewModel.saveWorkout(workoutDuration, false)
                }) {
                    Icon(asset = Icons.Default.Done)
                }
            }, navigationIcon = {
                IconButton(onClick = {
                    startWorkoutViewModel.cancelWorkout()
                }) {
                    Icon(asset = Icons.Default.Close)
                }
            })
        },
        bodyContent = {
            var scrollState = rememberScrollState(0f)

            Column(
                modifier = Modifier.fillMaxWidth().padding(it)
                    .padding(start = 16.dp, bottom = 30.dp, end = 16.dp),
            ) {
                if (workout != null) {
                    var workoutName by
                    remember { mutableStateOf(TextFieldValue(workout!!.workoutName)) }
                    BaseTextField(
                        value = workoutName,
                        onValueChange = {
                            workoutName = it
                            startWorkoutViewModel.updateWorkoutName(workoutName.text)
                        },
                        textStyle = MaterialTheme.typography.h5,
                        textColor = Color.White,
                        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                    )

                    val chronometerOnTick = Chronometer.OnChronometerTickListener {
                        workoutDuration = it.text.toString()
                    }

                    AndroidChronometer(chronometerOnTick)

                    Spacer(modifier = Modifier.preferredHeight(16.dp))

                    LazyColumnFor(items = workout!!.exercises) { exercise ->
                        Text(exercise.exerciseName, color = Color.White)

                        var setCount by remember { mutableStateOf(1) }

//                        WithConstraints {
//                            val width =
//                                with(DensityAmbient.current) { constraints.maxWidth.toDp() / 3 }
                            val modifier = Modifier.width(width = 40.dp)

                                Column(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                                ) {
                                    exercise.sets.forEach {
                                        SetRow(set = it, modifier = modifier)
                                    }
//                                scrollState.smoothScrollTo(scrollState.maxValue)

                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.fillMaxWidth()
                                            .padding(top = 8.dp, bottom = 20.dp)
                                    ) {
                                        TextButton(onClick = {
                                            //Save current sets to viewModel list of Exercises with sets
                                            startWorkoutViewModel.addSetToExercise(
                                                setCount = setCount,
                                                exercise.exerciseName
                                            )
                                            setCount = setCount.inc()
//                                        scrollState.smoothScrollTo(scrollState.maxValue)
                                        }, content = {
                                            Text("ADD SET")
                                        })
                                    }
                                }
//                            }
//                        }
                    }
                }
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(text = { Text("ADD EXERCISE") }, onClick = {
                ChooseExerciseDialogFragment(startWorkoutViewModel.listOfExercises.value!!).show(
                    parentFragmentManager,
                    ""
                )
            })
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            BottomAppBar(cutoutShape = CircleShape) {}
        })
}

@ExperimentalFoundationApi
@Composable
fun SetRow(
    set: WorkoutSet,
    modifier: Modifier
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        var setField by remember { mutableStateOf(TextFieldValue("${set.index}")) }
        Column(
            horizontalAlignment = ContentGravity.CenterHorizontally,
            modifier = modifier.padding(horizontal = 8.dp)
        ) {
            OutlinedTextField(
                value = setField,
                onValueChange = {
                    setField = it
                },
                label = {
                    Text("Set")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        var setWeightField by remember { mutableStateOf(TextFieldValue(set.weight)) }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(horizontal = 8.dp),
        ) {
            OutlinedTextField(
                value = setWeightField,
                onValueChange = {
                    setWeightField = it
                    set.weight = setWeightField.text
                },
                label = {
                    Text("Weight (lb)")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                onImeActionPerformed = { imeAction, softwareKeyboardController ->
                    if (imeAction == ImeAction.Done) {
                        softwareKeyboardController?.hideSoftwareKeyboard()
//                        scrollState.smoothScrollTo(scrollState.maxValue)
                    }
                }
            )
        }

        var setRepsField by remember { mutableStateOf(TextFieldValue(set.reps)) }
        Column(
            horizontalAlignment = ContentGravity.CenterHorizontally,
            modifier = modifier.padding(horizontal = 8.dp)
        ) {
            OutlinedTextField(
                value = setRepsField,
                onValueChange = {
                    setRepsField = it
                    set.reps = setRepsField.text
                },
                label = {
                    Text("Reps")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                onImeActionPerformed = { imeAction, softwareKeyboardController ->
                    if (imeAction == ImeAction.Done) {
                        softwareKeyboardController?.hideSoftwareKeyboard()
//                        scrollState.smoothScrollTo(scrollState.maxValue)
                    }
                }
            )
        }
    }
}

@Composable
fun AndroidChronometer(chronometerOnTick: Chronometer.OnChronometerTickListener) {
    AndroidView({ context ->
        val chronometer = Chronometer(context)
        chronometer.start()
        chronometer.onChronometerTickListener = chronometerOnTick
        return@AndroidView chronometer
    }, modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))
}
