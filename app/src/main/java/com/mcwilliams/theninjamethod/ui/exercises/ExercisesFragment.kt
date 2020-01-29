package com.mcwilliams.theninjamethod.ui.exercises

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.databinding.FragmentHomeBinding
import com.mcwilliams.theninjamethod.model.AddExerciseRequest
import com.mcwilliams.theninjamethod.model.Exercise
import com.mcwilliams.theninjamethod.ui.exercises.viewmodel.ExerciseListViewModel


class ExercisesFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: ExerciseListViewModel

    private var errorSnackbar: Snackbar? = null

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

        viewModel = ViewModelProviders.of(this).get(ExerciseListViewModel::class.java)

        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) showError(errorMessage) else hideError()
        })
        binding.exerciseListViewModel = viewModel

        binding.swipeContainer.setOnRefreshListener {
            viewModel.refreshData()
        }

        binding.addExercise.setOnClickListener {
            val view = layoutInflater.inflate(R.layout.add_exercise_dialog, null)
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
                .setTitle("Add Exercise")
                .setView(view)
                .setPositiveButton("Save") { _, _ ->
                    //do something
                    val exerciseName =
                        view.findViewById<TextInputEditText>(R.id.exerciseName).text.toString()
                    val exerciseType =
                        view.findViewById<TextInputEditText>(R.id.exerciseType).text.toString()
                    val exercise = Exercise(exerciseName, exerciseType, "")
                    val addExerciseRequest = AddExerciseRequest(exercise)
                    viewModel.addExercise(addExerciseRequest)
                }
                .setNegativeButton("Cancel") { _, _ ->
                    {

                    }
                }
            materialAlertDialogBuilder.show()
        }

    }

    private fun showError(@StringRes errorMessage: Int) {
        errorSnackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackbar?.setAction(R.string.retry, viewModel.errorClickListener)
        errorSnackbar?.show()
    }

    private fun hideError() {
        errorSnackbar?.dismiss()
    }
}
