package com.mcwilliams.theninjamethod.ui.workouts.manualworkoutdetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.textview.MaterialTextView
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.databinding.ShareWorkoutImageBinding
import com.mcwilliams.theninjamethod.ui.exercises.model.ExerciseType
import com.mcwilliams.theninjamethod.ui.workouts.manualworkoutdetail.db.Workout
import com.muddzdev.quickshot.QuickShot
import kotlinx.android.synthetic.main.share_workout_image.*
import java.text.NumberFormat
import java.time.LocalDateTime
import java.util.*

class ShareManualWorkoutFragment : Fragment(), QuickShot.QuickShotListener {
    lateinit var binding: ShareWorkoutImageBinding
    lateinit var workout: Workout
    var amountLifted: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        workout = arguments?.getSerializable("workout") as Workout
        amountLifted = arguments?.getInt("amountLifted")!!

        binding = DataBindingUtil.inflate(
            inflater, R.layout.share_workout_image, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val workoutName = view.findViewById<MaterialTextView>(R.id.workout_name)
        workoutName.text = workout.workoutName

        val totalWeightLifted = view.findViewById<MaterialTextView>(R.id.workout_weight_lifted)

        totalWeightLifted.text =
            "Weight Lifted: ${NumberFormat.getNumberInstance(Locale.US).format(amountLifted)}lbs"

        for (exercise in workout.exercises!!) {

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
                            "${set.reps} reps, "
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
            exercise_summary_layout.addView(shareExerciseSummary)
        }

        btnShare.setOnClickListener {
            share()
        }
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