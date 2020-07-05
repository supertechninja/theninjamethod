package com.mcwilliams.theninjamethod.ui.workouts.stravadetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.textview.MaterialTextView
import com.mcwilliams.theninjamethod.BuildConfig
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.databinding.ShareStravaWorkoutImageBinding
import com.mcwilliams.theninjamethod.strava.model.activitydetail.StravaActivityDetail
import com.muddzdev.quickshot.QuickShot
import kotlinx.android.synthetic.main.share_workout_image.*
import java.time.LocalDateTime
import kotlin.math.roundToInt

class ShareStravaWorkoutFragment : Fragment(), QuickShot.QuickShotListener {
    lateinit var binding: ShareStravaWorkoutImageBinding
    lateinit var workout: StravaActivityDetail

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        workout = arguments?.getSerializable("workout") as StravaActivityDetail

        binding = DataBindingUtil.inflate(
            inflater, R.layout.share_strava_workout_image, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.workoutName.text = workout.name
//        binding.mapView.load(getMapUrl(workout.map.summary_polyline))

        binding.distance.text = workout.miles
        binding.workoutDuration.text = workout.duration

        binding.caloriesBurned.text = workout.calories.toString()

        if (workout.splits_standard.isNullOrEmpty()) {
            binding.splits.visibility = View.GONE
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

                    binding.splits.addView(splitsRow)
                }
            }
        }

        btnShare.setOnClickListener {
            share()
        }
    }

    fun getMapUrl(polyline: String): String {
        return "https://maps.googleapis.com/maps/api/staticmap?size=600x300&scale=2&maptype=roadmap&path=enc:${polyline}&key=${BuildConfig.MAPS_API_KEY}"
    }

    private fun share() {
        QuickShot.of(binding.rootView).setResultListener(this)
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