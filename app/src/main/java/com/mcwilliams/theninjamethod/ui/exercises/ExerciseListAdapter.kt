package com.mcwilliams.theninjamethod.ui.exercises

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.databinding.ExerciseItemBinding
import com.mcwilliams.theninjamethod.ui.exercises.db.Exercise
import com.mcwilliams.theninjamethod.ui.exercises.viewmodel.ExerciseViewModel


class ExerciseListAdapter() : RecyclerView.Adapter<ExerciseListAdapter.ViewHolder>() {

    private lateinit var exerciseList: MutableList<Exercise>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ExerciseItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.exercise_item, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(exerciseList[position])
    }

    override fun getItemCount(): Int {
        return if (::exerciseList.isInitialized) exerciseList.size else 0
    }

    fun getExerciseList(): List<Exercise> {
        return exerciseList
    }

    fun updatePostList(postList: MutableList<Exercise>) {
        if (::exerciseList.isInitialized) {
            this.exerciseList.clear()
        }
        this.exerciseList = postList
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ExerciseItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val viewModel = ExerciseViewModel()
        fun bind(post: Exercise) {
            // ...
            viewModel.bind(post)
            binding.exerciseViewModel = viewModel
        }
    }
}