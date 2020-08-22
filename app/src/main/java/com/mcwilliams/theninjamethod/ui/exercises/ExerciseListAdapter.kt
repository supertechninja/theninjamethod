package com.mcwilliams.theninjamethod.ui.exercises

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.mcwilliams.data.exercisedb.DbExercise
import com.mcwilliams.theninjamethod.R
import kotlin.random.Random


class ExerciseListAdapter() : RecyclerView.Adapter<ExerciseListAdapter.ViewHolder>() {

    private lateinit var exerciseList: MutableList<DbExercise>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.exercise_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exercise = exerciseList[position]
        holder.exerciseName.text = exercise.exerciseName
        holder.exercisePerformedCount.text = Random.nextInt(0, 10).toString()

        if (!exercise.exerciseBodyPart.isNullOrEmpty()) {
            holder.exerciseBodyPart.text = exercise.exerciseBodyPart
        } else {
            holder.exerciseBodyPart.text = ""
        }
    }

    override fun getItemCount(): Int {
        return if (::exerciseList.isInitialized) exerciseList.size else 0
    }

    fun getExerciseList(): List<DbExercise> {
        return exerciseList
    }

    fun updatePostList(postList: MutableList<DbExercise>) {
        if (::exerciseList.isInitialized) {
            this.exerciseList.clear()
        }
        this.exerciseList = postList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val exerciseName = itemView.findViewById<MaterialTextView>(R.id.exercise_title)
        val exercisePerformedCount = itemView.findViewById<MaterialTextView>(R.id.exercise_count)
        val exerciseBodyPart = itemView.findViewById<MaterialTextView>(R.id.exercise_muscle_group)
    }
}