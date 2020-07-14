package com.mcwilliams.theninjamethod.ui.routines

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mcwilliams.theninjamethod.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RoutinesFragment : Fragment() {

    private val viewModel: RoutinesViewModel by viewModels()
    private val routinesAdapter = RoutinesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_routines, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val routineList = view.findViewById<RecyclerView>(R.id.routines_list)
        routineList.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        routineList.adapter = routinesAdapter

        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)

        viewModel.workout.observe(viewLifecycleOwner, Observer {
            Log.d("TAG", "onViewCreated: ${it.size}")
            routinesAdapter.updateRoutines(it)
            progressBar.visibility = View.GONE
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return super.onOptionsItemSelected(item)
    }

}

