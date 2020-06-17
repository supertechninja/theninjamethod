package com.mcwilliams.theninjamethod.ui.workouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.databinding.FragmentWorkoutsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkoutsFragment : Fragment() {

    private lateinit var binding: FragmentWorkoutsBinding

    private var errorSnackbar: Snackbar? = null

    private val viewModel: WorkoutListViewModel by viewModels()

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
            viewModel.dropWorkoutDb()
        }

        binding.startWorkout.setOnClickListener {
            viewModel.onStartWorkoutClick()
//            val view = layoutInflater.inflate(R.layout.add_exercise_dialog, null)
//            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
//                .setTitle("Add Exercise")
//                .setView(view)
//                .setPositiveButton("Save") { _, _ ->
//                }
//                .setNegativeButton("Cancel") { _, _ ->
//                    {
//
//                    }
//                }
//            materialAlertDialogBuilder.show()
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
