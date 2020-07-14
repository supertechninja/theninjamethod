package com.mcwilliams.theninjamethod.ui.activity.stravadetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.mcwilliams.theninjamethod.BuildConfig
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.strava.model.activitydetail.StravaActivityDetail
import com.muddzdev.quickshot.QuickShot
import java.time.LocalDateTime
import kotlin.math.roundToInt

class ShareStravaWorkoutFragment : Fragment(), QuickShot.QuickShotListener {
    lateinit var workout: StravaActivityDetail
    lateinit var rootView: ConstraintLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        workout = arguments?.getSerializable("workout") as StravaActivityDetail

        return inflater.inflate(R.layout.share_strava_workout_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rootView = view.findViewById(R.id.rootView)

        val workoutName = view.findViewById<MaterialTextView>(R.id.workout_name)
        workoutName.text = workout.name
//        binding.mapView.load(getMapUrl(workout.map.summary_polyline))

        val distance = view.findViewById<MaterialTextView>(R.id.distance)
        distance.text = workout.miles

        val duration = view.findViewById<MaterialTextView>(R.id.workout_duration)
        duration.text = workout.duration

        val caloriesBurned = view.findViewById<MaterialTextView>(R.id.calories_burned)
        caloriesBurned.text = workout.calories.toString()

        val llSplits = view.findViewById<LinearLayout>(R.id.splits)
        if (workout.splits_standard.isNullOrEmpty()) {
            llSplits.visibility = View.GONE
        } else {
            workout.splits_standard!!.forEach {
                if ((it.distance > 10.00)) {
                    val splitsRow = layoutInflater.inflate(R.layout.share_strava_split_row, null)

                    val splitCount = splitsRow.findViewById<MaterialTextView>(R.id.split_count)
                    splitCount.text = it.split.toString()

                    val splitDistance = splitsRow.findViewById<MaterialTextView>(R.id.split_pace)
                    val movingTime = "${it.moving_time / 60}m ${it.moving_time % 60}s"
                    splitDistance.text = movingTime

                    val splitTime = splitsRow.findViewById<MaterialTextView>(R.id.split_heart_rate)
                    splitTime.text = it.average_heartrate.roundToInt().toString()

                    llSplits.addView(splitsRow)
                }
            }
        }

        val btnShare = view.findViewById<MaterialButton>(R.id.btnShare)
        btnShare.setOnClickListener {
            share()
        }
    }

    fun getMapUrl(polyline: String): String {
        return "https://maps.googleapis.com/maps/api/staticmap?size=600x300&scale=2&maptype=roadmap&path=enc:${polyline}&key=${BuildConfig.MAPS_API_KEY}"
    }

    private fun share() {
        QuickShot.of(rootView).setResultListener(this)
            .setFilename("ninja-method-${LocalDateTime.now()}")
            .setPath("NinjaMethod")
            .toJPG()
            .save();

    }

    override fun onQuickShotSuccess(path: String?) {
        Log.d("TAG", "onQuickShotSuccess: $path")

        // Define image asset URI and attribution link URL
//        val backgroundAssetUri = Uri.parse(path)
//
//        val intent = Intent(Intent.ACTION_SEND)
//        intent.type = "image/*"
//        intent.putExtra(Intent.EXTRA_STREAM, backgroundAssetUri);
//
//        startActivity(Intent.createChooser(intent, "Share to"));

        Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
    }

    override fun onQuickShotFailed(path: String?) {
        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
    }
}