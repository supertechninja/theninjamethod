package com.mcwilliams.theninjamethod.ui.workouts.stravadetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import coil.api.load
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.ui.workouts.combinedworkoutlist.model.Workout
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

        viewModel.detailedActivity.observe(viewLifecycleOwner, Observer {
            workout_name.text = it.name
            map_view.load(getMapUrl(it.map.summary_polyline))
            workout_date.text = it.formattedDate

            distance.text = it.miles
            workout_duration.text = it.duration
        })

        viewModel.getDetailedActivities(workout.id)
    }

    fun getMapUrl(polyline: String): String {
        return "https://maps.googleapis.com/maps/api/staticmap?size=700x350&scale=2&maptype=roadmap&path=enc:${polyline}&key=AIzaSyBWhwSFZ1aFOyxGN057wR_4wMA3QMyLT9I"
    }


}