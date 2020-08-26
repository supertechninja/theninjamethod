package com.mcwilliams.theninjamethod.ui.activity.manualworkoutdetail

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.bold
import androidx.fragment.app.Fragment
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textview.MaterialTextView
import com.mcwilliams.data.exercisedb.model.ExerciseType
import com.mcwilliams.data.workoutdb.Workout
import com.mcwilliams.theninjamethod.R
import com.muddzdev.quickshot.QuickShot
import dev.sasikanth.colorsheet.ColorSheet
import kotlinx.android.synthetic.main.share_workout_image.*
import java.text.NumberFormat
import java.time.LocalDateTime
import java.util.*

class ShareManualWorkoutFragment : Fragment(), QuickShot.QuickShotListener {
    lateinit var workout: Workout
    var amountLifted: Int = 0
    lateinit var rootView: ConstraintLayout

    @ColorInt
    var selectedColor: Int = ColorSheet.NO_COLOR
    var noColorOption = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        workout = arguments?.getSerializable("workout") as Workout
        amountLifted = arguments?.getInt("amountLifted")!!

        return inflater.inflate(R.layout.share_workout_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val workoutName = view.findViewById<MaterialTextView>(R.id.workout_name)
        workoutName.text = workout.workoutName

        rootView = view.findViewById(R.id.rootView)

        val colors = resources.getIntArray(R.array.colors)
        selectedColor = colors.first()
        rootView.setBackgroundColor(selectedColor)

        val cardColor = view.findViewById<AppCompatImageButton>(R.id.cardColor)
        cardColor.setBackgroundColor(colors.first())
        cardColor.setOnClickListener {
            ColorSheet().cornerRadius(8)
                .colorPicker(
                    colors = colors,
                    noColorOption = false,
                    selectedColor = selectedColor,
                    listener = { color ->
                        selectedColor = color
                        rootView.setBackgroundColor(selectedColor)
                        cardColor.setBackgroundColor(selectedColor)
                    })
                .show(requireActivity().supportFragmentManager)
        }

        val totalWeightLifted = view.findViewById<MaterialTextView>(R.id.workout_weight_lifted)

        val switchWeightLifted = view.findViewById<SwitchMaterial>(R.id.switchWeightLifted)
        switchWeightLifted.isChecked = true
        switchWeightLifted.setOnCheckedChangeListener { compoundButton, boolean ->
            if (boolean) {
                totalWeightLifted.visibility = View.VISIBLE
            } else {
                totalWeightLifted.visibility = View.GONE
            }
        }

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

            val s = SpannableStringBuilder()
                .bold { append(exerciseName) }
                .append(": " + setsSummaryFormatted)


            exerciseSummary.text = s
            exercise_summary_layout.addView(shareExerciseSummary)
        }

        val workoutDuration = view.findViewById<MaterialTextView>(R.id.workout_duration)
        workoutDuration.text = "Time: ${workout.workoutDuration}"

        val switchDuration = view.findViewById<SwitchMaterial>(R.id.switchDuration)
        switchDuration.isChecked = true
        switchDuration.setOnCheckedChangeListener { compoundButton, boolean ->
            if (boolean) {
                workoutDuration.visibility = View.VISIBLE
            } else {
                workoutDuration.visibility = View.GONE
            }
        }

        btnShare.setOnClickListener {
            share()
        }
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