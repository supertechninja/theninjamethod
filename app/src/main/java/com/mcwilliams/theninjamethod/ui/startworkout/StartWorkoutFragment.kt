package com.mcwilliams.theninjamethod.ui.startworkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.WithConstraints
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.viewModel
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mcwilliams.data.exercisedb.model.ExerciseType
import com.mcwilliams.data.exercisedb.model.WorkoutExercise
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
                arguments?.getSerializable("workout") as com.mcwilliams.data.workoutdb.Workout
        }

        (activity as AppCompatActivity).supportActionBar!!.hide()

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

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar!!.show()
    }
}

class TestViewModel : ViewModel() {
    var _workout = MutableLiveData(Workout(0, "", "", listOf()))
    val workout: LiveData<Workout> = _workout

    fun addExercise(exerciseName: String) {
        //make a copy of the existing exercises
        val existingExercies = workout.value!!.exercises
        //create new exercise
        val workoutExercise =
            WorkoutExercise(exerciseName, ExerciseType.bodyweight, mutableListOf())
        // merge exercises into 1 list
        val combinedExercises = existingExercies + listOf(workoutExercise)
        //create new workout object with combined exercies
        val updatedWorkout = Workout(0, "", "", combinedExercises)
        //post value of new workout
        _workout.postValue(updatedWorkout)
    }

    fun addSetToExercise(setCount: Int, exerciseName: String) {
        //store the current workout sets
        val currentSets =
            workout.value!!.exercises.find { it.exerciseName == exerciseName }!!.sets
        //create the new workout set
        val newSet = WorkoutSet(setCount, "", "")
        //store the current exercises
        val existingExercies = workout.value!!.exercises
        //find the current exercise to update the sets on
        val currentExercise = existingExercies.find { it.exerciseName == exerciseName }
        //create a mutable list of existing exercises
        val list = existingExercies.toMutableList()
        //create an update the current exercise with an updated sets list
        list[existingExercies.indexOf(currentExercise)] =
            WorkoutExercise(exerciseName, ExerciseType.bodyweight, currentSets + listOf(newSet))
        //create a new workout object with the updated workout with the updated sets
        val updatedWorkout = Workout(0, "", "", list.toList())
        //post updated value of workout
        _workout.postValue(updatedWorkout)
    }
}

data class MockWorkout(var workoutExercies: List<WorkoutExercise> = listOf())


@Composable
fun ShowList() {
    val viewModel: TestViewModel = viewModel(TestViewModel::class.java)
    val workout by viewModel.workout.observeAsState()

    Scaffold(
        floatingActionButton = {
            var indexOfExercise by remember { mutableStateOf(0) }

            FloatingActionButton(
                onClick = {
                    val listOfExercises = listOf("Pullups", "Chin Ups", "Crunches")
                    viewModel.addExercise(listOfExercises[indexOfExercise])
                    indexOfExercise = indexOfExercise.inc()
                }, icon = { Icon(asset = Icons.Default.Add) })
        },
        bodyContent = {
            Column() {
                LazyColumnFor(items = workout!!.exercises) {
                    Text(text = it.exerciseName)

                    var set by remember { mutableStateOf(0) }
                    LazyColumnFor(
                        items = it.sets,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) { workoutSet ->
                        Text(text = "${workoutSet.index}")
                    }

                    TextButton(onClick = {
                        viewModel.addSetToExercise(set, it.exerciseName)
                        set = set.inc()
                    }) {
                        Text(text = "Set")
                    }
                }


            }
        }
    )
}

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val timeOfDay = getTimeOfDay()
//
//        chronometer = view.findViewById(R.id.chronometer)
//        chronometer.start()
//
//        val workoutName = view.findViewById<TextInputEditText>(R.id.workout_name)
//        exerciseList = view.findViewById(R.id.exercise_list)
//
//        //Observes changes to workout as the workout is built
//        startWorkoutViewModel.workout.observe(viewLifecycleOwner, Observer { workout ->
//            if (workout != null) {
//                workoutName.setText(workout.workoutName)
//                exerciseList.removeAllViews()
//                if (!workout.exercises.isNullOrEmpty()) {
//                    workout.exercises!!.forEach { exercise ->
//                        drawExerciseRow(exercise)
//                    }
//                }
//            }
//        })
//
//        if (workout != null) {
//            startWorkoutViewModel.createWorkoutFromRoutine(workout!!)
//            saveWorkoutAsRoutine = true
//        } else {
//            startWorkoutViewModel.createWorkout(timeOfDay)
//        }
//
//        val bookmarkWorkout = view.findViewById<AppCompatImageView>(R.id.bookmark_workout)
//        bookmarkWorkout.setOnClickListener {
//            saveWorkoutAsRoutine = !saveWorkoutAsRoutine
//            if (saveWorkoutAsRoutine) {
//                Snackbar.make(it, "Workout will be saved as Routine", Snackbar.LENGTH_SHORT).show()
//                bookmarkWorkout.setImageResource(R.drawable.bookmark_filled)
//            } else {
//                bookmarkWorkout.setImageResource(R.drawable.bookmark_outline)
//            }
//        }
//
//        //Observing the list of exercises to choose from during a workout
//        startWorkoutViewModel.listOfExercises.observe(viewLifecycleOwner, Observer { exercistList ->
//            if (!exercistList.isNullOrEmpty()) {
//                loadedExercises = exercistList
//            }
//        })
//
//        startWorkoutViewModel.didSaveWorkout.observe(viewLifecycleOwner, Observer {
//            if (it) {
//                startWorkoutViewModel._didSaveWorkout.postValue(false)
//                goBack()
//            }
//        })
//
//        workoutName.setOnEditorActionListener { v, actionId, event ->
//            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                startWorkoutViewModel.updateWorkoutName(workoutName.text.toString())
//                val inputMethodManager =
//                    requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
//                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
//                workoutName.clearFocus()
//                true
//            } else {
//                false
//            }
//        }
//
//        workoutName.setSelectAllOnFocus(true)
//
//        val addExercise = view.findViewById<MaterialButton>(R.id.add_exercise)
//        addExercise.setOnClickListener {
//            if (workoutName.text.toString() == "Set Workout Name") {
//                Snackbar.make(it, "Set Workout Name First", Snackbar.LENGTH_SHORT).show()
//            } else {
//                ChooseExerciseDialogFragment(loadedExercises).show(
//                    parentFragmentManager,
//                    ""
//                )
//            }
//        }
//
//        val cancelWorkout = view.findViewById<MaterialButton>(R.id.cancel_workout)
//        cancelWorkout.setOnClickListener {
//            startWorkoutViewModel.cancelWorkout()
//        }
//    }
//
//
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//        inflater.inflate(R.menu.start_workout_menu, menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (R.id.menu_done == item.itemId) {
//            chronometer.stop()
//            startWorkoutViewModel.saveWorkout(chronometer.text.toString(), saveWorkoutAsRoutine)
//        }
//        return super.onOptionsItemSelected(item)
//    }
//
//    private fun goBack() {
//        Navigation.findNavController(requireView()).popBackStack()
//    }
//
//    private fun drawExerciseRow(exercise: WorkoutExercise?) {
//        val addExerciseViewLayout =
//            layoutInflater.inflate(R.layout.add_exercise_row_view, null)
//        val exerciseNameView =
//            addExerciseViewLayout.findViewById<MaterialTextView>(R.id.exercise_name)
//
//        if (exercise != null) {
//            exerciseNameView.text = exercise!!.exerciseName
//            if (!exercise!!.sets.isNullOrEmpty()) {
//                exercise.sets!!.forEach {
//                    val addWorkoutSetLayout =
//                        layoutInflater.inflate(R.layout.add_exercise_sets_row_view, null)
//
//                    val setCount =
//                        addWorkoutSetLayout.findViewById<MaterialTextView>(R.id.set_count)
//                    setCount.text = it.index.toString()
//
//                    //Shows weight field and sets the text if any
//                    val weight =
//                        addWorkoutSetLayout.findViewById<TextInputEditText>(R.id.weight_amount)
//                    weight.setText(it.weight)
//
//                    if (it.weight.isEmpty()) {
//                        weight.requestFocus()
//                    }
//
//                    //Shows reps field and sets the text if any
//                    val reps = addWorkoutSetLayout.findViewById<TextInputEditText>(R.id.rep_count)
//                    if (it.reps.isNotEmpty()) {
//                        reps.setText(it.reps)
//                    }
//
//                    //Modify weight label based on exercise type
//                    val weightLabel =
//                        addExerciseViewLayout.findViewById<MaterialTextView>(R.id.weight_label)
//                    when (exercise.definedExerciseType) {
//                        ExerciseType.bodyweight -> {
//                            weightLabel.text = "(+LBS)"
//                        }
//                        else -> {
//                        }
//                    }
//
//                    val saveSet = addWorkoutSetLayout.findViewById<ImageView>(R.id.save_set)
//                    saveSet.setOnClickListener { view ->
//                        startWorkoutViewModel.updateSetInExercise(
//                            it.index,
//                            weight.text.toString(),
//                            reps.text.toString(),
//                            exercise.exerciseName
//                        )
//                    }
//
//                    if (it.weight.isNotEmpty() && it.reps.isNotEmpty()) {
//                        saveSet.imageTintList =
//                            ColorStateList.valueOf(
//                                ContextCompat.getColor(
//                                    requireContext(),
//                                    R.color.green
//                                )
//                            );
//                    }
//
//                    weight.setOnEditorActionListener { v, actionId, event ->
//                        if (actionId == EditorInfo.IME_ACTION_DONE) {
//                            startWorkoutViewModel.updateSetInExercise(
//                                it.index,
//                                weight.text.toString(),
//                                reps.text.toString(),
//                                exercise.exerciseName
//                            )
//                            true
//                        } else {
//                            false
//                        }
//                    }
////
////                    weight.setOnFocusChangeListener { view, focusChange ->
////                        if (!focusChange) {
////                            startWorkoutViewModel.updateSetInExercise(
////                                it.index,
////                                weight.text.toString(),
////                                reps.text.toString(),
////                                exercise.exerciseName
////                            )
////                        }
////                    }
//
//                    reps.setOnEditorActionListener { v, actionId, event ->
//                        if (actionId == EditorInfo.IME_ACTION_DONE) {
//                            startWorkoutViewModel.updateSetInExercise(
//                                it.index,
//                                weight.text.toString(),
//                                reps.text.toString(),
//                                exercise.exerciseName
//                            )
//                            true
//                        } else {
//                            false
//                        }
//                    }
//
////                    reps.setOnFocusChangeListener { view, focusChange ->
////                        if (!focusChange) {
////                            startWorkoutViewModel.updateSetInExercise(
////                                it.index,
////                                weight.text.toString(),
////                                reps.text.toString(),
////                                exercise.exerciseName
////                            )
////                        }
////                    }
//
//                    val addSet = addExerciseViewLayout.findViewById<MaterialButton>(R.id.add_set)
//                    addSet.setOnClickListener { view ->
//                        startWorkoutViewModel.updateSetInExercise(
//                            it.index,
//                            weight.text.toString(),
//                            reps.text.toString(),
//                            exercise.exerciseName
//                        )
//                        startWorkoutViewModel.addNewSetToExerciseToWorkout(exercise.exerciseName)
////            scrollViewContainer.scrollToBottom()
//                    }
//
//                    val exerciseSets =
//                        addExerciseViewLayout.findViewById<LinearLayout>(R.id.exercise_sets)
////                    addWorkoutSetLayout.setOnTouchListener(object : OnSwipeTouchListener() {
////                        override fun onSwipeLeft() {
////                            Snackbar.make(
////                                addExerciseViewLayout,
////                                "Are you sure you want to delete?",
////                                Snackbar.LENGTH_SHORT
////                            ).show()
////                        }
////
////                        override fun onSwipeRight() {
////
////                        }
////                    })
//                    exerciseSets.addView(addWorkoutSetLayout)
//                }
//            }
//        }
//
//        exerciseList.addView(addExerciseViewLayout)
//        exerciseList.requestFocus()
//    }
//}

fun ScrollView.scrollToBottom() {
    val lastChild = getChildAt(childCount - 1)
    val bottom = lastChild.bottom + paddingBottom
    val delta = bottom - (scrollY + height)
    smoothScrollBy(0, delta)
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
            })
        },
        bodyContent = {
            ScrollableColumn(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
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

                        WithConstraints {
                            val width =
                                with(DensityAmbient.current) { constraints.maxWidth.toDp() / 3 }
                            val modifier = Modifier.width(width = width)

                            Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                                LazyColumnFor(items = exercise.sets) {
                                    SetRow(set = it, modifier = modifier)
                                }

                                Column(
                                    horizontalGravity = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    TextButton(onClick = {
                                        //Save current sets to viewModel list of Exercises with sets
                                        startWorkoutViewModel.addSetToExercise(
                                            setCount = setCount,
                                            exercise.exerciseName
                                        )
                                        setCount = setCount.inc()

                                    }, content = {
                                        Text("ADD SET")
                                    })
                                }
                            }
                        }
                    }

//                    var indexOfExercise by remember { mutableStateOf(0) }
                    Column(
                        horizontalGravity = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    ) {
                        TextButton(onClick = {
//                            val listOfExercises = listOf("Pullups", "Chin Ups", "Crunches")
//                            startWorkoutViewModel.addExercise(listOfExercises[indexOfExercise])
//                            indexOfExercise = indexOfExercise.inc()
                            ChooseExerciseDialogFragment(startWorkoutViewModel.listOfExercises.value!!).show(
                                parentFragmentManager,
                                ""
                            )
                        }, content = {
                            Text("ADD EXERCISE")
                        })
                    }
                    Column(
                        horizontalGravity = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    ) {
                        TextButton(onClick = {}, content = {
                            Text("CANCEL WORKOUT")
                        })
                    }
                }
            }
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
            horizontalGravity = ContentGravity.CenterHorizontally,
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
            )
        }

        var setWeightField by remember { mutableStateOf(TextFieldValue(set.weight)) }
        Column(
            horizontalGravity = ContentGravity.CenterHorizontally,
            modifier = modifier.padding(horizontal = 8.dp),
        ) {
            OutlinedTextField(
                value = setWeightField,
                onValueChange = {
                    setWeightField = it
                    set.weight = setWeightField.text
                },
                label = {
                    Text("Weight")
                }
            )
        }

        var setRepsField by remember { mutableStateOf(TextFieldValue(set.reps)) }
        Column(
            horizontalGravity = ContentGravity.CenterHorizontally,
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
