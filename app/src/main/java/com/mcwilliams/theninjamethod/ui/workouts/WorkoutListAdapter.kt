package com.mcwilliams.theninjamethod.ui.workouts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.databinding.StravaWorkoutItemBinding
import com.mcwilliams.theninjamethod.databinding.WorkoutItemBinding
import com.mcwilliams.theninjamethod.model.Workout
import com.mcwilliams.theninjamethod.model.WorkoutType


class WorkoutListAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var workoutList: List<Workout>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == WorkoutType.LIFTING.ordinal) {
            val binding: WorkoutItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.workout_item, parent, false
            )
            WorkoutViewHolder(binding)
        } else {
            val binding: StravaWorkoutItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.strava_workout_item, parent, false
            )
            StravaWorkoutViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == WorkoutType.LIFTING.ordinal) {
            (holder as WorkoutViewHolder).bind((workoutList[position]), holder)
        } else {
            (holder as StravaWorkoutViewHolder).bind((workoutList[position]), holder)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (workoutList[position].workoutType == WorkoutType.LIFTING)
            WorkoutType.LIFTING.ordinal
        else {
            WorkoutType.STRAVA.ordinal
        }
    }

    override fun getItemCount(): Int {
        return if (::workoutList.isInitialized) workoutList.size else 0
    }

    fun updateWorkoutList(workoutDates: List<Workout>) {
        this.workoutList = workoutDates
        notifyDataSetChanged()
    }

    class WorkoutViewHolder(private val binding: WorkoutItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val viewModel = WorkoutViewModel()

        fun bind(workoutObj: Workout, holder: WorkoutViewHolder) {
            // ...
            viewModel.bind(workoutObj, holder)
            binding.workoutViewModel = viewModel
        }
    }

    class StravaWorkoutViewHolder(private val binding: StravaWorkoutItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val viewModel = StravaWorkoutViewModel()

        fun bind(workoutObj: Workout, holder: StravaWorkoutViewHolder) {
            // ...
            viewModel.bind(workoutObj, holder)
            binding.stravaWorkoutViewModel = viewModel
        }
    }
}