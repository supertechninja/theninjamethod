package com.mcwilliams.theninjamethod.ui.startworkout

import android.app.Activity
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist.model.Exercise
import com.mcwilliams.theninjamethod.ui.exercises.model.ExerciseType
import com.mcwilliams.theninjamethod.utils.OnSwipeTouchListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_start_workout.*
import java.util.*


@AndroidEntryPoint
class StartWorkoutFragment : Fragment() {

    lateinit var exerciseName: String
    lateinit var exerciseListView: LinearLayout
    lateinit var loadedExercises: List<com.mcwilliams.theninjamethod.ui.exercises.db.Exercise>

    private val startWorkoutViewModel: StartWorkoutViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_start_workout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val timeOfDay = getTimeOfDay()
        startWorkoutViewModel.createWorkout(timeOfDay)

        //Observing the list of exercises to choose from during a workout
        startWorkoutViewModel.listOfExercises.observe(viewLifecycleOwner, Observer { exercistList ->
            if (!exercistList.isNullOrEmpty()) {
                loadedExercises = exercistList
            }
        })

        //Observes changes to workout as the workout is built
        startWorkoutViewModel.workout.observe(viewLifecycleOwner, Observer { workout ->
            if (workout != null) {
                workout_name.setText(workout.workoutName)
                exercise_list.removeAllViews()
                if (!workout.exercises.isNullOrEmpty()) {
                    workout.exercises!!.forEach { exercise ->
                        drawExerciseRow(exercise)
                    }
                }
            }
        })

        startWorkoutViewModel.didSaveWorkout.observe(viewLifecycleOwner, Observer {
            if (it) goBack()
        })

        workout_name.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                startWorkoutViewModel.updateWorkoutName(workout_name.text.toString())
                val inputMethodManager =
                    requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                workout_name.clearFocus()
                true
            } else {
                false
            }
        }

        workout_name.setSelectAllOnFocus(true)

        add_exercise.setOnClickListener {
            if (workout_name.text.toString() == "Set Workout Name") {
                Snackbar.make(it, "Set Workout Name First", Snackbar.LENGTH_SHORT).show()
            } else {
                ChooseExerciseDialogFragment(loadedExercises).show(
                    parentFragmentManager,
                    ""
                )
            }
        }

        cancel_workout.setOnClickListener {
            startWorkoutViewModel.cancelWorkout()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.start_workout_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (R.id.menu_done == item.itemId) {
            startWorkoutViewModel.saveWorkout()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun goBack() {
        Navigation.findNavController(requireView()).popBackStack()
    }

    private fun drawExerciseRow(exercise: Exercise?) {
        val addExerciseViewLayout =
            layoutInflater.inflate(R.layout.add_exercise_row_view, null)
        val exerciseNameView =
            addExerciseViewLayout.findViewById<MaterialTextView>(R.id.exercise_name)

//        if (isFirstDraw) {
//            exerciseNameView.text = "CHOOSE EXERCISE"
//            exerciseNameView.setTextColor(
//                ContextCompat.getColor(
//                    requireContext(),
//                    R.color.color_secondary
//                )
//            )
//            exerciseNameView.setTypeface(null, Typeface.BOLD)
//        } else {

//        }

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
                    addWorkoutSetLayout.setOnTouchListener(object : OnSwipeTouchListener() {
                        override fun onSwipeLeft() {
                            Snackbar.make(
                                addExerciseViewLayout,
                                "Are you sure you want to delete?",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }

                        override fun onSwipeRight() {

                        }
                    })
                    exerciseSets.addView(addWorkoutSetLayout)
                }
            }
        }

        exercise_list.addView(addExerciseViewLayout)
        exercise_list.requestFocus()
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

