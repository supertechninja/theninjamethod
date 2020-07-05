package com.mcwilliams.theninjamethod.ui.workouts.combinedworkoutlist.combineddetail

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import coil.api.load
import com.google.android.material.textview.MaterialTextView
import com.mcwilliams.theninjamethod.BuildConfig
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.databinding.FragmentCombinedWorkoutDetailBinding
import com.mcwilliams.theninjamethod.ui.workouts.combinedworkoutlist.model.Workout
import com.mcwilliams.theninjamethod.ui.workouts.combinedworkoutlist.model.WorkoutSet
import com.mcwilliams.theninjamethod.ui.workouts.combinedworkoutlist.model.WorkoutType
import com.mcwilliams.theninjamethod.utils.extensions.fixCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_combined_workout_detail.*
import kotlinx.android.synthetic.main.strava_workout_detail_fragment.*
import java.text.NumberFormat
import java.time.LocalDate
import java.util.*

@AndroidEntryPoint
class CombinedWorkoutDetailFragment : Fragment() {
    lateinit var combinedWorkout: Pair<LocalDate, MutableList<Workout>>
    lateinit var rootView: FragmentCombinedWorkoutDetailBinding
    private val viewModel: CombinedWorkoutViewModel by viewModels()
    var totalAmountLifted = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        combinedWorkout =
            arguments?.getSerializable("workoutSummary") as Pair<LocalDate, MutableList<Workout>>
        setHasOptionsMenu(true)
        rootView = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_combined_workout_detail,
            container,
            false
        )
        return rootView.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val workout = combinedWorkout.second
        workout.forEach {
            when (it.workoutType) {
                WorkoutType.LIFTING -> {
                    viewModel.getManualWorkoutDetail(it.id)
                }
                WorkoutType.STRAVA -> {
                    viewModel.getDetailedActivities(it.id)
                }
            }
        }

        rootView.workoutName.text = combinedWorkout.first.dayOfWeek.name.fixCase() +
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

            for (exercise in it.exercises) {

                val shareExerciseSummary =
                    layoutInflater.inflate(R.layout.share_workout_exercise_summary, null)
                val exerciseSummary =
                    shareExerciseSummary.findViewById<MaterialTextView>(R.id.exercise_summary)
                val exerciseName = exercise.exerciseName

                var setsSummary = ""
                for (set in exercise.sets) {
                    setsSummary += "${set.reps}x${set.weight}lbs, "
                }

                totalWeightLifted(exercise.sets)

                val setsSummaryFormatted = setsSummary.substring(0, (setsSummary.length - 2))

                exerciseSummary.text = "$exerciseName: $setsSummaryFormatted"
                exercisesListLayout.addView(shareExerciseSummary)
            }

            val workoutWeightLifted =
                manualWorkoutCardView.findViewById<MaterialTextView>(R.id.tvWeightLifted)
            workoutWeightLifted.text =
                NumberFormat.getNumberInstance(Locale.US).format(totalAmountLifted) + "lbs lifted"

            workout_card_container.addView(manualWorkoutCardView)
        })

        viewModel.detailedActivity.observe(viewLifecycleOwner, Observer { stravaDetail ->
            //load strava workout
            val stravaWorkoutCardview =
                layoutInflater.inflate(R.layout.combined_strava_workout_cardview, null)

            val workoutName =
                stravaWorkoutCardview.findViewById<MaterialTextView>(R.id.workout_name)
            workoutName.text = stravaDetail.name

            val mapView =
                stravaWorkoutCardview.findViewById<ImageView>(R.id.map_view)

            if (stravaDetail.map!!.summary_polyline.isNullOrEmpty()) {
                map_view.visibility = View.GONE
            } else {
                map_view.load(getMapUrl(stravaDetail.map.summary_polyline!!))
            }

            val workoutDistance =
                stravaWorkoutCardview.findViewById<MaterialTextView>(R.id.distance)
            workoutDistance.text = stravaDetail.miles

            val workoutDuration =
                stravaWorkoutCardview.findViewById<MaterialTextView>(R.id.workout_duration)
            workoutDuration.text = stravaDetail.miles

            val caloriesBurned =
                stravaWorkoutCardview.findViewById<MaterialTextView>(R.id.calories_burned)
            caloriesBurned.text = stravaDetail.calories.toString()

            workout_card_container.addView(stravaWorkoutCardview)
        })
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
                "workout" to combinedWorkout
            )
            Navigation.findNavController(rootView.root)
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