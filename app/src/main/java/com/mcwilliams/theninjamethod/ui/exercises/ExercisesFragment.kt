package com.mcwilliams.theninjamethod.ui.exercises

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.databinding.FragmentHomeBinding
import com.mcwilliams.theninjamethod.ui.exercises.db.Exercise
import com.mcwilliams.theninjamethod.ui.exercises.viewmodel.ExerciseListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExercisesFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private var errorSnackbar: Snackbar? = null

    private val viewModel: ExerciseListViewModel by viewModels()
    private val exerciseListAdapter: ExerciseListAdapter = ExerciseListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflate(
            inflater, R.layout.fragment_home, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.exerciseList.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        binding.exerciseListViewModel = viewModel
        binding.exerciseList.adapter = exerciseListAdapter

        binding.swipeContainer.setOnRefreshListener {
            viewModel.refreshData()
        }

        viewModel.exerciseList.observe(viewLifecycleOwner, Observer {
            exerciseListAdapter.updatePostList(it)
            binding.progressBar.visibility = View.GONE
        })

        binding.addExercise.setOnClickListener {
            val exerciseDialog = layoutInflater.inflate(R.layout.add_exercise_dialog, null)
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
                .setTitle("Add Exercise")
                .setView(view)
                .setPositiveButton("Save") { _, _ ->
                    val exerciseName =
                        exerciseDialog.findViewById<TextInputEditText>(R.id.exerciseName).text.toString()
                    val exerciseType =
                        exerciseDialog.findViewById<TextInputEditText>(R.id.exerciseType).text.toString()
                    val exerciseBodyPart =
                        exerciseDialog.findViewById<TextInputEditText>(R.id.exerciseBodyPart).text.toString()

                    val exercise = Exercise(0, exerciseName, exerciseType, exerciseBodyPart)
                    viewModel.addNewExercise(exercise)
                }
                .setNegativeButton("Cancel") { _, _ ->
                    {

                    }
                }
            materialAlertDialogBuilder.show()
        }

    }

//    private fun showError(@StringRes errorMessage: Int) {
//        errorSnackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
//        errorSnackbar?.setAction(R.string.retry, viewModel.errorClickListener)
//        errorSnackbar?.show()
//    }

    private fun hideError() {
        errorSnackbar?.dismiss()
    }
}
