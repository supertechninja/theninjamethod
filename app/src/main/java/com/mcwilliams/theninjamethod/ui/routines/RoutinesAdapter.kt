package com.mcwilliams.theninjamethod.ui.routines

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.ui.activity.manualworkoutdetail.db.Workout


class RoutinesAdapter() : RecyclerView.Adapter<RoutinesAdapter.ViewHolder>() {

    private lateinit var routinesList: List<Workout>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.exercise_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.routineName.text = routinesList[position].workoutName
    }

    override fun getItemCount(): Int {
        return if (::routinesList.isInitialized) routinesList.size else 0
    }

    fun getRoutinesList(): List<Workout> {
        return routinesList
    }

    fun updateRoutines(routineList: List<Workout>) {
//        if (::routinesList.isInitialized) {
//            this.routinesList.clear()
//        }
        this.routinesList = routineList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val routineName = itemView.findViewById<MaterialTextView>(R.id.exercise_title)
//        private val viewModel = ExerciseViewModel()
//        fun bind(post: Exercise) {
//            // ...
//            viewModel.bind(post)
//            binding.exerciseViewModel = viewModel
//        }
    }
}