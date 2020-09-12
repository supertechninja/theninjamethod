package com.mcwilliams.theninjamethod.ui.startworkout

import android.app.Activity
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Chronometer
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.mcwilliams.data.exercisedb.DbExercise
import com.mcwilliams.data.exercisedb.model.ExerciseType
import com.mcwilliams.data.exercisedb.model.WorkoutExercise
import com.mcwilliams.theninjamethod.R
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class StartWorkoutFragment : Fragment() {

    var workout: com.mcwilliams.data.workoutdb.Workout? = null
    lateinit var exerciseName: String
    lateinit var chronometer: Chronometer
    lateinit var loadedExercises: List<DbExercise>
    lateinit var exerciseList: LinearLayout
    var saveWorkoutAsRoutine = false
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

        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_start_workout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val timeOfDay = getTimeOfDay()

        chronometer = view.findViewById(R.id.chronometer)
        chronometer.start()

        val workoutName = view.findViewById<TextInputEditText>(R.id.workout_name)
        exerciseList = view.findViewById(R.id.exercise_list)

        //Observes changes to workout as the workout is built
        startWorkoutViewModel.workout.observe(viewLifecycleOwner, Observer { workout ->
            if (workout != null) {
                workoutName.setText(workout.workoutName)
                exerciseList.removeAllViews()
                if (!workout.exercises.isNullOrEmpty()) {
                    workout.exercises!!.forEach { exercise ->
                        drawExerciseRow(exercise)
                    }
                }
            }
        })

        if (workout != null) {
            startWorkoutViewModel.createWorkoutFromRoutine(workout!!)
            saveWorkoutAsRoutine = true
        } else {
            startWorkoutViewModel.createWorkout(timeOfDay)
        }

        val bookmarkWorkout = view.findViewById<AppCompatImageView>(R.id.bookmark_workout)
        bookmarkWorkout.setOnClickListener {
            saveWorkoutAsRoutine = !saveWorkoutAsRoutine
            if (saveWorkoutAsRoutine) {
                Snackbar.make(it, "Workout will be saved as Routine", Snackbar.LENGTH_SHORT).show()
                bookmarkWorkout.setImageResource(R.drawable.bookmark_filled)
            } else {
                bookmarkWorkout.setImageResource(R.drawable.bookmark_outline)
            }
        }

        //Observing the list of exercises to choose from during a workout
        startWorkoutViewModel.listOfExercises.observe(viewLifecycleOwner, Observer { exercistList ->
            if (!exercistList.isNullOrEmpty()) {
                loadedExercises = exercistList
            }
        })

        startWorkoutViewModel.didSaveWorkout.observe(viewLifecycleOwner, Observer {
            if (it) goBack()
        })

        workoutName.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                startWorkoutViewModel.updateWorkoutName(workoutName.text.toString())
                val inputMethodManager =
                    requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                workoutName.clearFocus()
                true
            } else {
                false
            }
        }

        workoutName.setSelectAllOnFocus(true)

        val addExercise = view.findViewById<MaterialButton>(R.id.add_exercise)
        addExercise.setOnClickListener {
            if (workoutName.text.toString() == "Set Workout Name") {
                Snackbar.make(it, "Set Workout Name First", Snackbar.LENGTH_SHORT).show()
            } else {
                ChooseExerciseDialogFragment(loadedExercises).show(
                    parentFragmentManager,
                    ""
                )
            }
        }

        val cancelWorkout = view.findViewById<MaterialButton>(R.id.cancel_workout)
        cancelWorkout.setOnClickListener {
            startWorkoutViewModel.cancelWorkout()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.start_workout_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (R.id.menu_done == item.itemId) {
            chronometer.stop()
            startWorkoutViewModel.saveWorkout(chronometer.text.toString())
            if (saveWorkoutAsRoutine) {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun goBack() {
        Navigation.findNavController(requireView()).popBackStack()
    }

    private fun drawExerciseRow(exercise: WorkoutExercise?) {
        val addExerciseViewLayout =
            layoutInflater.inflate(R.layout.add_exercise_row_view, null)
        val exerciseNameView =
            addExerciseViewLayout.findViewById<MaterialTextView>(R.id.exercise_name)

        if (exercise != null) {
            exerciseNameView.text = exercise!!.exerciseName
            if (!exercise!!.sets.isNullOrEmpty()) {
                exercise.sets!!.forEach {
                    val addWorkoutSetLayout =
                        layoutInflater.inflate(R.layout.add_exercise_sets_row_view, null)

                    val setCount =
                        addWorkoutSetLayout.findViewById<MaterialTextView>(R.id.set_count)
                    setCount.text = it.index.toString()

                    //Shows weight field and sets the text if any
                    val weight =
                        addWorkoutSetLayout.findViewById<TextInputEditText>(R.id.weight_amount)
                    weight.setText(it.weight)

                    if (it.weight.isEmpty()) {
                        weight.requestFocus()
                    }

                    //Shows reps field and sets the text if any
                    val reps = addWorkoutSetLayout.findViewById<TextInputEditText>(R.id.rep_count)
                    if (it.reps.isNotEmpty()) {
                        reps.setText(it.reps)
                    }

                    //Modify weight label based on exercise type
                    val weightLabel =
                        addExerciseViewLayout.findViewById<MaterialTextView>(R.id.weight_label)
                    when (exercise.definedExerciseType) {
                        ExerciseType.bodyweight -> {
                            weightLabel.text = "(+LBS)"
                        }
                        else -> {
                        }
                    }

                    val saveSet = addWorkoutSetLayout.findViewById<ImageView>(R.id.save_set)
                    saveSet.setOnClickListener { view ->
                        startWorkoutViewModel.updateSetInExercise(
                            it.index,
                            weight.text.toString(),
                            reps.text.toString(),
                            exercise.exerciseName
                        )
                    }

                    if (it.weight.isNotEmpty() && it.reps.isNotEmpty()) {
                        saveSet.imageTintList =
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.green
                                )
                            );
                    }

                    weight.setOnEditorActionListener { v, actionId, event ->
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            startWorkoutViewModel.updateSetInExercise(
                                it.index,
                                weight.text.toString(),
                                reps.text.toString(),
                                exercise.exerciseName
                            )
                            true
                        } else {
                            false
                        }
                    }
//
//                    weight.setOnFocusChangeListener { view, focusChange ->
//                        if (!focusChange) {
//                            startWorkoutViewModel.updateSetInExercise(
//                                it.index,
//                                weight.text.toString(),
//                                reps.text.toString(),
//                                exercise.exerciseName
//                            )
//                        }
//                    }

                    reps.setOnEditorActionListener { v, actionId, event ->
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            startWorkoutViewModel.updateSetInExercise(
                                it.index,
                                weight.text.toString(),
                                reps.text.toString(),
                                exercise.exerciseName
                            )
                            true
                        } else {
                            false
                        }
                    }

//                    reps.setOnFocusChangeListener { view, focusChange ->
//                        if (!focusChange) {
//                            startWorkoutViewModel.updateSetInExercise(
//                                it.index,
//                                weight.text.toString(),
//                                reps.text.toString(),
//                                exercise.exerciseName
//                            )
//                        }
//                    }

                    val addSet = addExerciseViewLayout.findViewById<MaterialButton>(R.id.add_set)
                    addSet.setOnClickListener { view ->
                        startWorkoutViewModel.updateSetInExercise(
                            it.index,
                            weight.text.toString(),
                            reps.text.toString(),
                            exercise.exerciseName
                        )
                        startWorkoutViewModel.addNewSetToExerciseToWorkout(exercise.exerciseName)
//            scrollViewContainer.scrollToBottom()
                    }

                    val exerciseSets =
                        addExerciseViewLayout.findViewById<LinearLayout>(R.id.exercise_sets)
//                    addWorkoutSetLayout.setOnTouchListener(object : OnSwipeTouchListener() {
//                        override fun onSwipeLeft() {
//                            Snackbar.make(
//                                addExerciseViewLayout,
//                                "Are you sure you want to delete?",
//                                Snackbar.LENGTH_SHORT
//                            ).show()
//                        }
//
//                        override fun onSwipeRight() {
//
//                        }
//                    })
                    exerciseSets.addView(addWorkoutSetLayout)
                }
            }
        }

        exerciseList.addView(addExerciseViewLayout)
        exerciseList.requestFocus()
    }
}

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

//@ExperimentalFoundationApi
//@Composable
//fun StartWorkoutFrame() {
//    val startWorkoutViewModel = viewModel(StartWorkoutViewModel::class.java)
//    val workout by startWorkoutViewModel.workout.observeAsState()
//
//    val timeOfDay = getTimeOfDay()
//    startWorkoutViewModel.updateWorkoutName(timeOfDay)
//
//    ScrollableColumn(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
//
//        if (workout != null) {
//            var workoutName = remember { TextFieldValue(workout!!.workoutName) }
//            BaseTextField(
//                value = workoutName,
//                onValueChange = {
//                    workoutName = it
//                    startWorkoutViewModel.updateWorkoutName(workoutName.text)
//                },
//                textStyle = MaterialTheme.typography.h6,
//                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
//            )
//
//            AndroidChronometer()
//
////        val exercisesPerformed = remember { workout!!.exercises }
////            val exercises by mutableStateOf(workout!!.exercises)
//            val exercisesPerformed by startWorkoutViewModel.liveListOfExercisesPerformed.observeAsState()
////            if(exercisesPerformed.v.size > 0) {
//            exercisesPerformed!!.forEach { exercise ->
//                Text(exercise.exerciseName)
//                ExerciseSetHeaderRow(exercise)
//            }
////            }
//
//            Column(
//                horizontalGravity = Alignment.CenterHorizontally,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                TextButton(onClick = {
//                    startWorkoutViewModel.addExerciseToWorkout("Pullups", ExerciseType.bodyweight)
//                }, content = {
//                    Text("ADD EXERCISE")
//                })
//            }
//            Column(
//                horizontalGravity = Alignment.CenterHorizontally,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                TextButton(onClick = {}, content = {
//                    Text("CANCEL WORKOUT")
//                })
//            }
//        }
//    }
//}
//
//@ExperimentalFoundationApi
//@Composable
//fun ExerciseSetHeaderRow(exercise: WorkoutExercise?) {
//    Column() {
//        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
//            Text("SET")
//            Text("WEIGHT")
//            Text("REPS")
//            Text("")
//        }
//        exercise?.let { exercise ->
//            exercise.sets?.let { workoutSets ->
//                workoutSets.forEach { set ->
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceEvenly
//                    ) {
//                        Text("${set.index}")
//
//                        var setWeightField = remember { TextFieldValue("") }
//                        BaseTextField(
//                            value = setWeightField,
//                            onValueChange = {
//                                setWeightField = it
//                            }
//                        )
//
//                        var setRepsField = remember { TextFieldValue("") }
//                        BaseTextField(
//                            value = setRepsField,
//                            onValueChange = {
//                                setRepsField = it
//                            }
//                        )
//
//                        Image(asset = vectorResource(R.drawable.ic_iconfinder_checkmark))
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun AndroidChronometer() {
//    AndroidView({ context ->
//        val chronometer = Chronometer(context)
//        chronometer.start()
//        return@AndroidView chronometer
//    }, modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))
//}
