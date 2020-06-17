package com.mcwilliams.theninjamethod.ui.workouts

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.databinding.StravaWorkoutItemBinding
import com.mcwilliams.theninjamethod.databinding.WorkoutCardItemViewBinding
import com.mcwilliams.theninjamethod.databinding.WorkoutItemBinding
import com.mcwilliams.theninjamethod.ui.workouts.ui.model.Workout
import com.mcwilliams.theninjamethod.ui.workouts.ui.model.WorkoutType
import java.time.LocalDate


class WorkoutListAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var workoutList: List<Pair<LocalDate, MutableList<Workout>>>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return if (viewType == WorkoutType.LIFTING.ordinal) {
//            val binding: WorkoutItemBinding = DataBindingUtil.inflate(
//                LayoutInflater.from(parent.context),
//                R.layout.workout_item, parent, false
//            )
//            WorkoutViewHolder(binding)
//        } else {
            val binding: StravaWorkoutItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.strava_workout_item, parent, false
            )
            return StravaWorkoutViewHolder(binding)
//        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        if (getItemViewType(position) == WorkoutType.LIFTING.ordinal) {
//            (holder as WorkoutViewHolder).bind((workoutList[position]), holder)
//        } else {
            (holder as StravaWorkoutViewHolder).bind(workoutList[position], holder)
//        }
    }

    override fun getItemViewType(position: Int): Int {
//        return if (workoutList[position].workoutType == WorkoutType.LIFTING)
//            WorkoutType.LIFTING.ordinal
//        else {
           return WorkoutType.STRAVA.ordinal
//        }
    }

    override fun getItemCount(): Int {
        return if (::workoutList.isInitialized) workoutList.size else 0
    }

    fun updateWorkoutList(workoutDates: List<Pair<LocalDate, MutableList<Workout>>>) {
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

        @SuppressLint("SetTextI18n")
        fun bind(workoutObj: Pair<LocalDate, MutableList<Workout>>, holder: StravaWorkoutViewHolder) {
            val workoutDate = workoutObj.first
            holder.binding.stravaWorkoutTitle.text = workoutDate.dayOfWeek.name.toLowerCase().capitalize() +
                    ", " + workoutDate.month.name.toLowerCase().capitalize() + " " + workoutDate.dayOfMonth

            workoutObj.second.forEach {
                val workoutItemView = WorkoutCardItemViewBinding.inflate(LayoutInflater.from(holder.itemView.context))
                workoutItemView.workoutName.text = it.workoutName
                workoutItemView.workoutDuration.text = it.stravaTime
                workoutItemView.root.setOnClickListener { onWorkoutClicked(workoutItemView.root) }
                holder.binding.llWorkouts.addView(workoutItemView.root)
            }
        }

        private fun onWorkoutClicked(view: View){
            Navigation.findNavController(view).navigate(R.id.navigate_to_workout_detail)
        }
    }
}