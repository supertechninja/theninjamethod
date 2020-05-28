package com.mcwilliams.theninjamethod.ui.workouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.databinding.FragmentHomeBinding
import com.mcwilliams.theninjamethod.databinding.FragmentWorkoutsBinding
import com.mcwilliams.theninjamethod.model.AddExerciseRequest
import com.mcwilliams.theninjamethod.model.Exercise
import com.mcwilliams.theninjamethod.ui.exercises.viewmodel.ExerciseListViewModel
import com.mcwilliams.theninjamethod.utils.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class WorkoutsFragment : Fragment() {

    private lateinit var binding: FragmentWorkoutsBinding

    private var errorSnackbar: Snackbar? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: WorkoutListViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_workouts, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.workoutList.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
            if (errorMessage != null) showError(errorMessage) else hideError()
        })
        binding.workoutListViewModel = viewModel

        binding.swipeContainer.setOnRefreshListener {
            viewModel.refreshData()
        }

        binding.addExercise.setOnClickListener {
            val view = layoutInflater.inflate(R.layout.add_exercise_dialog, null)
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
                .setTitle("Add Exercise")
                .setView(view)
                .setPositiveButton("Save") { _, _ ->
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
