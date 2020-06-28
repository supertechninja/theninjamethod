package com.mcwilliams.theninjamethod.ui.workouts.stravadetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import coil.api.load
import com.google.android.material.textview.MaterialTextView
import com.mcwilliams.theninjamethod.BuildConfig
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.ui.workouts.combinedworkoutlist.model.Workout
import com.mcwilliams.theninjamethod.utils.extensions.getMiles
import com.mcwilliams.theninjamethod.utils.extensions.round
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.strava_workout_detail_fragment.*

@AndroidEntryPoint
class StravaWorkoutDetailFragment : Fragment() {
    lateinit var workout: Workout
    private val viewModel: StravaDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        workout = arguments?.getSerializable("workout") as Workout
        return inflater.inflate(R.layout.strava_workout_detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getDetailedActivities(workout.id)
        viewModel.detailedActivity.observe(viewLifecycleOwner, Observer { stravaDetail ->
            workout_name.text = stravaDetail.name

            map_view.load(getMapUrl(stravaDetail.map.summary_polyline))
            workout_date.text = stravaDetail.formattedDate

            distance.text = stravaDetail.miles
            workout_duration.text = stravaDetail.duration

            calories_burned.text = stravaDetail.calories.toString()

            if (stravaDetail.has_heartrate) {
                workout_heartrate.text = "${stravaDetail.average_heartrate} bpm"
            } else {
                workout_heartrate.visibility = View.GONE
                heartIcon.visibility = View.GONE
            }

            stravaDetail.splits_standard.forEach {
                val splitsRow = layoutInflater.inflate(R.layout.strava_split_row, null)

                val splitCount = splitsRow.findViewById<MaterialTextView>(R.id.split_count)
                splitCount.text = it.split.toString()

                val splitDistance = splitsRow.findViewById<MaterialTextView>(R.id.split_distance)
                splitDistance.text = it.distance.getMiles().round(2).toString() + " mi"

                val splitTime = splitsRow.findViewById<MaterialTextView>(R.id.split_time)
                val movingTime = "${it.moving_time / 60}m ${it.moving_time % 60}s"
                splitTime.text = movingTime

                llsplits.addView(splitsRow)
            }
        })

    }

    fun getMapUrl(polyline: String): String {
        return "https://maps.googleapis.com/maps/api/staticmap?size=700x350&scale=2&maptype=roadmap&path=enc:${polyline}&key=${BuildConfig.MAPS_API_KEY}"
    }


}