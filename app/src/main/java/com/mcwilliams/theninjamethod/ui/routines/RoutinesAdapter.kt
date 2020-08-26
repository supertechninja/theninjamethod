package com.mcwilliams.theninjamethod.ui.routines

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.mcwilliams.theninjamethod.R


class RoutinesAdapter() : RecyclerView.Adapter<RoutinesAdapter.ViewHolder>() {

    private lateinit var routinesList: MutableList<com.mcwilliams.data.workoutdb.Workout>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.routine_row_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.routineName.text = routinesList[position].workoutName
        holder.buttonStartWorkout.setOnClickListener {
            val bundle = bundleOf("workout" to routinesList[position])
            Navigation.findNavController(holder.itemView)
                .navigate(R.id.navigate_from_routines_to_start_workout, bundle)
        }
    }

    override fun getItemCount(): Int {
        return if (::routinesList.isInitialized) routinesList.size else 0
    }

    fun getRoutinesList(): List<com.mcwilliams.data.workoutdb.Workout> {
        return routinesList
    }

    fun setRoutinesList(routineList: MutableList<com.mcwilliams.data.workoutdb.Workout>) {
        this.routinesList.clear()
        this.routinesList = routineList
        notifyDataSetChanged()
    }

    fun updateRoutines(routineList: MutableList<com.mcwilliams.data.workoutdb.Workout>) {
//        if (::routinesList.isInitialized) {
//            this.routinesList.clear()
//        }
        this.routinesList = routineList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val routineName = itemView.findViewById<MaterialTextView>(R.id.routine_name)
        val buttonStartWorkout = itemView.findViewById<MaterialButton>(R.id.btnStartWorkout)

//        private val viewModel = ExerciseViewModel()
//        fun bind(post: Exercise) {
//            // ...
//            viewModel.bind(post)
//            binding.exerciseViewModel = viewModel
//        }
    }
}