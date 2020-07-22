package com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist.model.Workout
import com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist.model.WorkoutType
import com.mcwilliams.theninjamethod.utils.extensions.fixCase
import java.time.LocalDate


class WorkoutListAdapter() : RecyclerView.Adapter<WorkoutListAdapter.ViewHolder>() {

    var workoutList: MutableList<Pair<LocalDate, MutableList<Workout>>> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.strava_workout_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val workoutObj = workoutList[position]
        val workoutDate = workoutObj.first
        holder.workoutTile.text =
            workoutDate.dayOfWeek.name.fixCase() +
                    ", " + workoutDate.month.name.fixCase() + " " + workoutDate.dayOfMonth

        workoutObj.second.forEach { workout ->
            val workoutItemView =
                LayoutInflater.from(holder.itemView.context)
                    .inflate(R.layout.workout_card_item_view, null)

            val workoutName = workoutItemView.findViewById<MaterialTextView>(R.id.workout_name)
            workoutName.text = workout.workoutName

            val distanceIcon = workoutItemView.findViewById<AppCompatImageView>(R.id.distance_icon)
            val durationIcon = workoutItemView.findViewById<AppCompatImageView>(R.id.duration_icon)

            val caloriesBurnedLayout =
                workoutItemView.findViewById<RelativeLayout>(R.id.rlCaloriesBurned)
            val caloriesBurned =
                workoutItemView.findViewById<MaterialTextView>(R.id.calories_burned)

            val distance = workoutItemView.findViewById<MaterialTextView>(R.id.distance)
            val duration = workoutItemView.findViewById<MaterialTextView>(R.id.duration)

            duration.text = workout.stravaTime
            distance.text = workout.stravaDistance

            if (workout.workoutType == WorkoutType.STRAVA) {
                distanceIcon.setImageResource(R.drawable.ic_distance)
                durationIcon.setImageResource(R.drawable.ic_clock)
            } else {
                distanceIcon.setImageResource(R.drawable.ic_weight)
                durationIcon.setImageResource(R.drawable.ic_clock)
            }

            if (workout.workoutCaloriesBurned.isNullOrEmpty()) {
                caloriesBurnedLayout.visibility = View.GONE
            } else {
                caloriesBurned.text = workout.workoutCaloriesBurned
            }

            distanceIcon.imageTintList =
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.color_on_background
                    )
                )
            durationIcon.imageTintList =
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.color_on_background
                    )
                )

            workoutItemView.setOnClickListener {
                holder.onWorkoutClicked(
                    workoutItemView,
                    workout
                )
            }

            holder.llWorkout.addView(workoutItemView)
        }

        holder.itemView.setOnClickListener {
            val bundle = bundleOf("workoutSummary" to workoutObj)
            Navigation.findNavController(it)
                .navigate(R.id.navigate_to_combined_workout, bundle)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return WorkoutType.STRAVA.ordinal
    }

    override fun getItemCount(): Int {
        return workoutList.size
    }

    fun updateWorkoutList(workoutDates: MutableList<Pair<LocalDate, MutableList<Workout>>>) {
        this.workoutList = workoutDates
        notifyDataSetChanged()
    }

    fun clearData() {
        this.workoutList.clear()
    }

    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val workoutTile = itemView.findViewById<MaterialTextView>(R.id.stravaWorkoutTitle)
        val llWorkout = itemView.findViewById<LinearLayout>(R.id.llWorkouts)

        fun onWorkoutClicked(
            view: View,
            workout: Workout
        ) {
            val bundle = bundleOf("workout" to workout)
            when (workout.workoutType) {
                WorkoutType.STRAVA -> {
                    Navigation.findNavController(view)
                        .navigate(R.id.navigate_to_strava_workout_detail, bundle)
                }
                WorkoutType.LIFTING -> {
                    Navigation.findNavController(view)
                        .navigate(R.id.navigate_to_manual_workout_detail, bundle)
                }
            }

        }
    }
}