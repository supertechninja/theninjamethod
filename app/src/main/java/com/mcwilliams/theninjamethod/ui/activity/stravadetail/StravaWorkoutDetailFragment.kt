package com.mcwilliams.theninjamethod.ui.activity.stravadetail

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.google.android.material.textview.MaterialTextView
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.strava.model.activitydetail.StravaActivityDetail
import com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist.model.Workout
import com.mcwilliams.theninjamethod.utils.extensions.getTimeFloat
import com.mcwilliams.theninjamethod.utils.extensions.getTimeString
import com.robinhood.spark.SparkAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.strava_workout_detail_fragment.*


@AndroidEntryPoint
class StravaWorkoutDetailFragment : Fragment() {
    lateinit var workout: Workout
    private val viewModel: StravaDetailViewModel by viewModels()
    lateinit var rootView: View
    lateinit var stravaDetailObj: StravaActivityDetail

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        workout = arguments?.getSerializable("workout") as Workout
        setHasOptionsMenu(true)
        rootView = inflater.inflate(R.layout.strava_workout_detail_fragment, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progress_bar.visibility = View.VISIBLE
        content_view.visibility = View.GONE

        viewModel.getDetailedActivities(workout.id)
        viewModel.detailedActivity.observe(viewLifecycleOwner, Observer { stravaDetail ->
            stravaDetailObj = stravaDetail
            workout_name.text = stravaDetail.name

            if (stravaDetail.map!!.summary_polyline.isNullOrEmpty()) {
                map_view.visibility = View.GONE
            } else {
//                map_view.load(getMapUrl(stravaDetail.map.summary_polyline!!))
            }

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

            var splitsPaceArray: MutableList<Float> = mutableListOf()

            if (stravaDetail.splits_standard.isNullOrEmpty()) {
                llsplits.visibility = View.GONE
            } else {
                stravaDetail.splits_standard.forEach {
                    if ((it.distance > 50.00)) {
                        val splitsRow = layoutInflater.inflate(R.layout.strava_split_row, null)

                        val splitCount = splitsRow.findViewById<MaterialTextView>(R.id.split_count)
                        splitCount.text = it.split.toString()

                        val splitDistance =
                            splitsRow.findViewById<MaterialTextView>(R.id.split_distance)
                        splitDistance.text = it.moving_time.getTimeString()

                        val splitTime = splitsRow.findViewById<MaterialTextView>(R.id.split_time)
                        splitTime.text = it.average_heartrate.toInt().toString() + " bpm"

                        splitsPaceArray.add(it.moving_time.getTimeFloat())

                        llsplits.addView(splitsRow)
                    }
                }
            }

            if (stravaDetail.splits_standard.isNullOrEmpty()) {
                sparkview.visibility = View.GONE
            } else {
                sparkview.adapter = MyAdapter(splitsPaceArray.toFloatArray())
                sparkview.isScrubEnabled = true
                sparkview.setScrubListener { value ->
                    split_detail.text = getString(R.string.scrub_format, value)
                }
            }

            progress_bar.visibility = View.GONE
            content_view.visibility = View.VISIBLE
        })
    }

//    fun getMapUrl(polyline: String): String {
//        return "https://maps.googleapis.com/maps/api/staticmap?size=700x350&scale=2&maptype=roadmap&path=enc:${polyline}&key=${BuildConfig.MAPS_API_KEY}"
//    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.strava_workout_detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (R.id.menu_share == item.itemId) {
            val bundle = bundleOf(
                "workout" to stravaDetailObj
            )
            Navigation.findNavController(rootView)
                .navigate(R.id.navigate_to_share_strava_workout, bundle)
        }
        return super.onOptionsItemSelected(item)
    }


}


class MyAdapter(private val yData: FloatArray) : SparkAdapter() {
    override fun getCount(): Int {
        return yData.size
    }

    override fun getItem(index: Int): Any {
        return yData[index]
    }

    override fun getY(index: Int): Float {
        return yData[index]
    }
}


