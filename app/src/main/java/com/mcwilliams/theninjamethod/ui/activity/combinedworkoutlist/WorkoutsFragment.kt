package com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.ui.startworkout.StartWorkoutViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_workouts.*


@AndroidEntryPoint
class WorkoutsFragment : Fragment() {

    private var errorSnackbar: Snackbar? = null

    private val viewModel: WorkoutListViewModel by viewModels()
    private val startWorkoutViewModel: StartWorkoutViewModel by activityViewModels()

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

        val workoutListAdapter = WorkoutListAdapter()
        workout_list.adapter = workoutListAdapter

        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            if (it) {
                progress_bar.visibility = View.VISIBLE
            } else {
                progress_bar.visibility = View.GONE
            }
        })

        viewModel.workoutMapLiveData.observe(viewLifecycleOwner, Observer {
            Log.d("TAG", "onViewCreated: ${it.size}")
            //Clearing current data before rendering new data
            workoutListAdapter.clearData()
            workoutListAdapter.updateWorkoutList(it)
            viewModel._isLoading.postValue(false)
        })

        swipeContainer.setOnRefreshListener {
            viewModel.refreshData()
        }

        startWorkout.setOnClickListener {
            startWorkoutViewModel._didSaveWorkout.postValue(false)
            Navigation.findNavController(it).navigate(R.id.navigate_to_start_workout)
        }

        if (startWorkoutViewModel.workoutInProgress != null) {
            Snackbar.make(requireView(), "Workout In Progress", Snackbar.LENGTH_INDEFINITE)
                .setAction("Resume") {
                    Navigation.findNavController(requireView())
                        .navigate(R.id.navigate_to_start_workout)
                }.show()
        }
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
