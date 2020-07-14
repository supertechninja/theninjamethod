package com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
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

            val distanceLabel = workoutItemView.findViewById<MaterialTextView>(R.id.distance_label)
            val durationLabel = workoutItemView.findViewById<MaterialTextView>(R.id.duration_label)

            if (workout.workoutType == WorkoutType.STRAVA) {
                distanceLabel.text = "Distance"
                durationLabel.text = "Duration"
            } else {
                distanceLabel.text = "Weight Lifted"
                durationLabel.text = "Duration"
            }

            val distance = workoutItemView.findViewById<MaterialTextView>(R.id.distance)
            val duration = workoutItemView.findViewById<MaterialTextView>(R.id.duration)

            duration.text = workout.stravaTime
            distance.text = workout.stravaDistance

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