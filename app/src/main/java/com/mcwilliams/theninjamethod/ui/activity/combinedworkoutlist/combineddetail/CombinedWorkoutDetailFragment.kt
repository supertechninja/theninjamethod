package com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist.combineddetail

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import coil.api.load
import com.google.android.material.textview.MaterialTextView
import com.mcwilliams.theninjamethod.BuildConfig
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.strava.model.activitydetail.StravaActivityDetail
import com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist.model.Workout
import com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist.model.WorkoutSet
import com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist.model.WorkoutType
import com.mcwilliams.theninjamethod.ui.exercises.model.ExerciseType
import com.mcwilliams.theninjamethod.utils.extensions.fixCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_combined_workout_detail.*
import java.text.NumberFormat
import java.time.LocalDate
import java.util.*

@AndroidEntryPoint
class CombinedWorkoutDetailFragment : Fragment() {
    lateinit var combinedWorkout: Pair<LocalDate, MutableList<Workout>>
    private val viewModel: CombinedWorkoutViewModel by viewModels()
    var totalAmountLifted = 0
    lateinit var rootView: ConstraintLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        combinedWorkout =
            arguments?.getSerializable("workoutSummary") as Pair<LocalDate, MutableList<Workout>>
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_combined_workout_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listOfStravaWorkouts: MutableList<Workout> = mutableListOf()
        val listOfManualWorkouts: MutableList<Workout> = mutableListOf()
        for (workout in combinedWorkout.second) {
            when (workout.workoutType) {
                WorkoutType.LIFTING -> {
                    listOfManualWorkouts.add(workout)
                }
                WorkoutType.STRAVA -> {
                    listOfStravaWorkouts.add(workout)
                }
            }
        }

        if (listOfManualWorkouts.isNotEmpty()) {
            if (listOfManualWorkouts.size > 1) {
//            val ids = listOfStravaWorkouts.map { it.id }
//            viewModel.getManualWorkoutDetail(ids)
            } else {
                viewModel.getManualWorkoutDetail(listOfManualWorkouts[0].id)
            }
        }

        if (listOfStravaWorkouts.isNotEmpty()) {
            if (listOfStravaWorkouts.size > 1) {
                val ids = listOfStravaWorkouts.map { it.id }
                viewModel.getMultipleDetailedActivities(ids)
            } else {
                viewModel.getDetailedActivities(listOfStravaWorkouts[0].id)
            }
        }

        rootView = view.findViewById(R.id.rootView)

        val workoutName = view.findViewById<MaterialTextView>(R.id.workout_name)
        workoutName.text = combinedWorkout.first.dayOfWeek.name.fixCase() +
                ", " + combinedWorkout.first.month.name.fixCase() + " " + combinedWorkout.first.dayOfMonth

        viewModel.workout.observe(viewLifecycleOwner, Observer {
            //load manual workout
            //TODO setup for view reuse between combined and individual detail
            val manualWorkoutCardView =
                layoutInflater.inflate(R.layout.combined_manual_workout_cardview, null)

            val workoutName =
                manualWorkoutCardView.findViewById<MaterialTextView>(R.id.tvWorkoutName)
            workoutName.text = it.workoutName

            val exercisesListLayout =
                manualWorkoutCardView.findViewById<LinearLayout>(R.id.llexercises)

            for (exercise in it.exercises!!) {

                val shareExerciseSummary =
                    layoutInflater.inflate(R.layout.share_workout_exercise_summary, null)
                val exerciseSummary =
                    shareExerciseSummary.findViewById<MaterialTextView>(R.id.exercise_summary)
                val exerciseName = exercise.exerciseName

                var setsSummary = ""
                for (set in exercise.sets!!) {
                    val weightAndRepsString = when (exercise.definedExerciseType) {
                        ExerciseType.bodyweight -> {
                            if (set.weight.toInt() > 0) {
                                "${set.reps}x +${set.weight}lbs, "
                            } else {
                                "${set.reps}, "
                            }
                        }
                        else -> {
                            "${set.reps}x${set.weight}lbs, "
                        }
                    }
                    setsSummary += weightAndRepsString

                }

                val setsSummaryFormatted = setsSummary.substring(0, (setsSummary.length - 2))
                exerciseSummary.text = "$exerciseName: $setsSummaryFormatted"

                totalWeightLifted(exercise.sets)

                exercisesListLayout.addView(shareExerciseSummary)
            }

            val workoutWeightLifted =
                manualWorkoutCardView.findViewById<MaterialTextView>(R.id.tvWeightLifted)

            if (totalAmountLifted > 0) {
                workoutWeightLifted.text =
                    NumberFormat.getNumberInstance(Locale.US)
                        .format(totalAmountLifted) + "lbs lifted"
            } else {
                workoutWeightLifted.visibility = View.GONE
            }
            workout_card_container.addView(manualWorkoutCardView)
        })

        viewModel.detailedActivity.observe(viewLifecycleOwner, Observer { stravaDetail ->
            //load strava workout
            drawStravaDetailCards(stravaDetail)
        })

        viewModel.detailedActivities.observe(viewLifecycleOwner, Observer { stravaDetail ->
            //load strava workout
            stravaDetail.forEach {
                drawStravaDetailCards(it)
            }
        })
    }

    fun drawStravaDetailCards(stravaDetail: StravaActivityDetail) {
        val stravaWorkoutCardview =
            layoutInflater.inflate(R.layout.combined_strava_workout_cardview, null)

        val workoutName =
            stravaWorkoutCardview.findViewById<MaterialTextView>(R.id.workout_name)
        workoutName.text = stravaDetail.name

        val mapView =
            stravaWorkoutCardview.findViewById<ImageView>(R.id.map_view)

        if (stravaDetail.map!!.summary_polyline.isNullOrEmpty()) {
            mapView.visibility = View.GONE
        } else {
            mapView.load(getMapUrl(stravaDetail.map.summary_polyline!!))
        }

        val workoutDistance =
            stravaWorkoutCardview.findViewById<MaterialTextView>(R.id.distance)
        workoutDistance.text = stravaDetail.miles

        val workoutDuration =
            stravaWorkoutCardview.findViewById<MaterialTextView>(R.id.workout_duration)
        workoutDuration.text = stravaDetail.duration

        val caloriesBurned =
            stravaWorkoutCardview.findViewById<MaterialTextView>(R.id.calories_burned)
        caloriesBurned.text = stravaDetail.calories.toString()

        workout_card_container.addView(stravaWorkoutCardview)
    }

    fun getMapUrl(polyline: String): String {
        return "https://maps.googleapis.com/maps/api/staticmap?size=700x350&scale=2&maptype=roadmap&path=enc:${polyline}&key=${BuildConfig.MAPS_API_KEY}"
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.strava_workout_detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (R.id.menu_share == item.itemId) {
            val bundle = bundleOf(
                "manualworkout" to combinedWorkout
            )
            Navigation.findNavController(rootView)
                .navigate(R.id.navigate_to_share_combined_workout, bundle)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun totalWeightLifted(sets: List<WorkoutSet>) {
        sets.forEach {
            totalAmountLifted += (it.weight.toInt() * it.reps.toInt())
        }
    }

}