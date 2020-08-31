package com.mcwilliams.theninjamethod.ui.activity.stravadetail

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.google.android.material.textview.MaterialTextView
import com.mcwilliams.data.workoutdb.SimpleWorkout
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.strava.model.activitydetail.StravaActivityDetail
import com.mcwilliams.theninjamethod.utils.extensions.getTimeFloat
import com.mcwilliams.theninjamethod.utils.extensions.getTimeString
import com.robinhood.spark.SparkAdapter
import com.robinhood.spark.SparkView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StravaWorkoutDetailFragment : Fragment() {
    lateinit var workout: SimpleWorkout
    private val viewModel: StravaDetailViewModel by viewModels()
    lateinit var rootView: View
    lateinit var stravaDetailObj: StravaActivityDetail

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        workout = arguments?.getSerializable("workout") as SimpleWorkout
        setHasOptionsMenu(true)
        rootView = inflater.inflate(R.layout.strava_workout_detail_fragment, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)
        val contentView = view.findViewById<ConstraintLayout>(R.id.content_view)


        progressBar.visibility = View.VISIBLE
        contentView.visibility = View.GONE

        viewModel.getDetailedActivities(workout.id)
        viewModel.detailedActivity.observe(viewLifecycleOwner, Observer { stravaDetail ->
            stravaDetailObj = stravaDetail

            val workoutName = view.findViewById<MaterialTextView>(R.id.workout_name)
            workoutName.text = stravaDetail.name

            val mapView = view.findViewById<ImageView>(R.id.map_view)

            if (stravaDetail.map!!.summary_polyline.isNullOrEmpty()) {
                mapView.visibility = View.GONE
            } else {
//                map_view.load(getMapUrl(stravaDetail.map.summary_polyline!!))
            }

            val workoutDate = view.findViewById<MaterialTextView>(R.id.workout_date)
            workoutDate.text = stravaDetail.formattedDate

            val distance = view.findViewById<MaterialTextView>(R.id.distance)
            distance.text = stravaDetail.miles

            val workoutDuration = view.findViewById<MaterialTextView>(R.id.workout_duration)
            workoutDuration.text = stravaDetail.duration

            val caloriesBurned = view.findViewById<MaterialTextView>(R.id.calories_burned)
            caloriesBurned.text = stravaDetail.calories.toString()

            val workoutHeartrate = view.findViewById<MaterialTextView>(R.id.workout_heartrate)
            val heartIcon = view.findViewById<ImageView>(R.id.heartIcon)

            if (stravaDetail.has_heartrate) {
                workoutHeartrate.text = "${stravaDetail.average_heartrate} bpm"
            } else {
                workoutHeartrate.visibility = View.GONE
                heartIcon.visibility = View.GONE
            }

            var splitsPaceArray: MutableList<Float> = mutableListOf()


            val llsplits = view.findViewById<LinearLayout>(R.id.llsplits)

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

            val sparkview = view.findViewById<SparkView>(R.id.sparkview)
            val splitDetail = view.findViewById<MaterialTextView>(R.id.split_detail)

            if (stravaDetail.splits_standard.isNullOrEmpty()) {
                sparkview.visibility = View.GONE
            } else {
                sparkview.adapter = MyAdapter(splitsPaceArray.toFloatArray())
                sparkview.isScrubEnabled = true
                sparkview.setScrubListener { value ->
                    splitDetail.text = getString(R.string.scrub_format, value)
                }
            }

            progressBar.visibility = View.GONE
            contentView.visibility = View.VISIBLE
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


