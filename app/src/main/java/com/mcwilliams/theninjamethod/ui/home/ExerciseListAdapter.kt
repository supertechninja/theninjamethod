package com.mcwilliams.theninjamethod.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.databinding.ExerciseItemBinding
import com.mcwilliams.theninjamethod.model.Exercise
import com.mcwilliams.theninjamethod.viewmodel.ExerciseViewModel

class ExerciseListAdapter : RecyclerView.Adapter<ExerciseListAdapter.ViewHolder>() {
    private lateinit var postList:List<Exercise>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ExerciseItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.exercise_item, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(postList[position])
    }

    override fun getItemCount(): Int {
        return if(::postList.isInitialized) postList.size else 0
    }

    fun updatePostList(postList:List<Exercise>){
        this.postList = postList
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ExerciseItemBinding):RecyclerView.ViewHolder(binding.root){
        private val viewModel = ExerciseViewModel()
        fun bind(post:Exercise){
            // ...
            viewModel.bind(post)
            binding.exerciseViewModel = viewModel
        }
    }
}