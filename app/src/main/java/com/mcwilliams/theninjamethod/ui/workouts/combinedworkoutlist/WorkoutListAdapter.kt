package com.mcwilliams.theninjamethod.ui.workouts.combinedworkoutlist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.databinding.StravaWorkoutItemBinding
import com.mcwilliams.theninjamethod.databinding.WorkoutCardItemViewBinding
import com.mcwilliams.theninjamethod.ui.workouts.combinedworkoutlist.model.Workout
import com.mcwilliams.theninjamethod.ui.workouts.combinedworkoutlist.model.WorkoutType
import com.mcwilliams.theninjamethod.utils.extensions.fixCase
import java.time.LocalDate


class WorkoutListAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var workoutList: MutableList<Pair<LocalDate, MutableList<Workout>>> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: StravaWorkoutItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.strava_workout_item, parent, false
        )
        return StravaWorkoutViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as StravaWorkoutViewHolder).bind(workoutList[position], holder)
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

    class StravaWorkoutViewHolder(private val binding: StravaWorkoutItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(
            workoutObj: Pair<LocalDate, MutableList<Workout>>,
            holder: StravaWorkoutViewHolder
        ) {
            val workoutDate = workoutObj.first
            holder.binding.stravaWorkoutTitle.text =
                workoutDate.dayOfWeek.name.fixCase() +
                        ", " + workoutDate.month.name.fixCase() + " " + workoutDate.dayOfMonth

            workoutObj.second.forEach { workout ->
                val workoutItemView =
                    WorkoutCardItemViewBinding.inflate(LayoutInflater.from(holder.itemView.context))
                workoutItemView.workoutName.text = workout.workoutName

                if (workout.workoutType == WorkoutType.STRAVA) {
                    workoutItemView.distanceLabel.text = "Distance"
                    workoutItemView.durationLabel.text = "Duration"
                } else {
                    workoutItemView.distanceLabel.text = "Weight Lifted"
                    workoutItemView.durationLabel.text = "Duration"
                }

                workoutItemView.duration.text = workout.stravaTime
                workoutItemView.distance.text = workout.stravaDistance

                workoutItemView.root.setOnClickListener {
                    onWorkoutClicked(
                        workoutItemView.root,
                        workout
                    )
                }
                holder.binding.llWorkouts.addView(workoutItemView.root)
            }

            holder.binding.root.setOnClickListener {
                val bundle = bundleOf("workoutSummary" to workoutObj)
                Navigation.findNavController(it)
                    .navigate(R.id.navigate_to_combined_workout, bundle)
            }

        }

        private fun onWorkoutClicked(
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