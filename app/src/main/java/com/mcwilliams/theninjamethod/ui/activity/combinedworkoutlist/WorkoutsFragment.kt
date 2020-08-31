package com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.ui.startworkout.StartWorkoutViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkoutsFragment : Fragment() {

    private var errorSnackbar: Snackbar? = null

    private val viewModel: WorkoutListViewModel by viewModels()
    private val startWorkoutViewModel: StartWorkoutViewModel by activityViewModels()

    private lateinit var preferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_workouts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferences = requireContext().getSharedPreferences(
            requireContext().getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )
        if (!preferences.getBoolean("hasRetrievedRoutines", false)) {
            setupRoutines()
            preferences.edit().putBoolean("hasRetrievedRoutines", true).apply()
        }

        val workoutList = view.findViewById<RecyclerView>(R.id.workout_list)
        workoutList.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
            if (errorMessage != null) showError(errorMessage) else hideError()
        })

        val workoutListAdapter = WorkoutListAdapter()
        workoutList.adapter = workoutListAdapter

        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)
        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            if (it) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        })

        viewModel.workoutMapLiveData.observe(viewLifecycleOwner, Observer {
            Log.d("TAG", "onViewCreated: ${it.size}")
            //Clearing current data before rendering new data
            workoutListAdapter.clearData()
            workoutListAdapter.updateWorkoutList(it)
            viewModel._isLoading.postValue(false)
        })

        val swipeContainer = view.findViewById<SwipeRefreshLayout>(R.id.swipeContainer)
        swipeContainer.setOnRefreshListener {
            viewModel.refreshData()
        }

        val startWorkout = view.findViewById<ExtendedFloatingActionButton>(R.id.startWorkout)
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

    private fun setupRoutines() {
        val jsonfile: String =
            requireActivity().assets.open("routines.json").bufferedReader().use { it.readText() }
        val gson = Gson()
        val workout = gson.fromJson(jsonfile, com.mcwilliams.data.workoutdb.Workout::class.java)
        viewModel.prePopulateRoutines(workout)
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
