package com.mcwilliams.theninjamethod.ui.startworkout

import android.app.Activity
import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
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
import com.mcwilliams.theninjamethod.ui.workouts.combinedworkoutlist.model.Exercise
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_start_workout.*


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
                        drawExerciseRow(false, exercise)
                    }
                } else {
                    drawExerciseRow(true, null)
                }
            }
        })

        startWorkoutViewModel.didSaveWorkout.observe(viewLifecycleOwner, Observer {
            if (it) goBack()
        })

        workout_name.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                startWorkoutViewModel.createWorkout(workout_name.text.toString())
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

    private fun drawExerciseRow(isFirstDraw: Boolean, exercise: Exercise?) {
        val addExerciseViewLayout =
            layoutInflater.inflate(R.layout.add_exercise_row_view, null)
        val exerciseNameView =
            addExerciseViewLayout.findViewById<MaterialTextView>(R.id.exercise_name)

        if (isFirstDraw) {
            exerciseNameView.text = "CHOOSE EXERCISE"
            exerciseNameView.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.color_secondary
                )
            )
            exerciseNameView.setTypeface(null, Typeface.BOLD)
        } else {
            exerciseNameView.text = exercise!!.exerciseName
        }
        exerciseNameView.setOnClickListener {
            //Setup exercise picker dialog or full screen dialog or bottom sheet
            ChooseExerciseDialogFragment(loadedExercises).show(
                parentFragmentManager,
                ""
            )
        }

        if (exercise != null) {
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
                    exerciseSets.addView(addWorkoutSetLayout)
                }
            }
        }

        exercise_list.addView(addExerciseViewLayout)
    }
}

fun ScrollView.scrollToBottom() {
    val lastChild = getChildAt(childCount - 1)
    val bottom = lastChild.bottom + paddingBottom
    val delta = bottom - (scrollY + height)
    smoothScrollBy(0, delta)
}

