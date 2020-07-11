package com.mcwilliams.theninjamethod.ui.workouts.combinedworkoutlist.combineddetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.mcwilliams.theninjamethod.BuildConfig
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.databinding.ShareStravaWorkoutImageBinding
import com.mcwilliams.theninjamethod.ui.workouts.combinedworkoutlist.model.Workout
import com.muddzdev.quickshot.QuickShot
import kotlinx.android.synthetic.main.share_workout_image.*
import java.time.LocalDate
import java.time.LocalDateTime

class ShareCombinedWorkoutFragment : Fragment(), QuickShot.QuickShotListener {
    lateinit var binding: ShareStravaWorkoutImageBinding
    lateinit var workout: Pair<LocalDate, MutableList<Workout>>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        workout = arguments?.getSerializable("workout") as Pair<LocalDate, MutableList<Workout>>

        binding = DataBindingUtil.inflate(
            inflater, R.layout.share_strava_workout_image, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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