package com.mcwilliams.theninjamethod.ui.workouts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.databinding.ExerciseItemBinding
import com.mcwilliams.theninjamethod.databinding.WorkoutItemBinding
import com.mcwilliams.theninjamethod.model.Workout


class WorkoutListAdapter() :
    RecyclerView.Adapter<WorkoutListAdapter.ViewHolder>() {

    private lateinit var workoutList: List<Workout>


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: WorkoutItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.workout_item, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(workoutList[position])
    }

    override fun getItemCount(): Int {
        return if (::workoutList.isInitialized) workoutList.size else 0
    }

    fun updateWorkoutList(workoutDates: List<Workout>) {
        this.workoutList = workoutDates
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: WorkoutItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val viewModel = WorkoutViewModel()

        fun bind(workoutObj: Workout) {
            // ...
            viewModel.bind(workoutObj)
            binding.workoutViewModel = viewModel
        }
    }
}