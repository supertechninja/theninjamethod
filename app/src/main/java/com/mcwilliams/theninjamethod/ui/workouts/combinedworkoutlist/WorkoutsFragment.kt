package com.mcwilliams.theninjamethod.ui.workouts.combinedworkoutlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mcwilliams.theninjamethod.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_workouts.*


@AndroidEntryPoint
class WorkoutsFragment : Fragment() {

    private var errorSnackbar: Snackbar? = null

    private val viewModel: WorkoutListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_workouts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        workout_list.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
            if (errorMessage != null) showError(errorMessage) else hideError()
        })

        workout_list.adapter = viewModel.workoutListAdapter

        swipeContainer.setOnRefreshListener {
            viewModel.refreshData()
        }

        startWorkout.setOnClickListener {
//            viewModel.onStartWorkoutClick()
            Navigation.findNavController(it).navigate(R.id.navigate_to_start_workout)
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.refreshData()
    }

    private fun showError(@StringRes errorMessage: Int) {
//        errorSnackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
//        errorSnackbar?.setAction(R.string.retry, viewModel.errorClickListener)
//        errorSnackbar?.show()
    }

    private fun hideError() {
        errorSnackbar?.dismiss()
    }
}
