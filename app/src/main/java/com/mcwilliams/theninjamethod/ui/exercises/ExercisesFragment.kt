package com.mcwilliams.theninjamethod.ui.exercises

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.databinding.FragmentHomeBinding
import com.mcwilliams.theninjamethod.ui.exercises.viewmodel.ExerciseListViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ExercisesFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
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
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.exerciseList.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        binding.exerciseListViewModel = viewModel
        binding.exerciseList.adapter = exerciseListAdapter

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.exerciseList)

        viewModel.exerciseList.observe(viewLifecycleOwner, Observer {
            exerciseListAdapter.updatePostList(it)
            binding.progressBar.visibility = View.GONE
        })

        binding.addExercise.setOnClickListener {
            AddExerciseDialog().show(parentFragmentManager, "TAG")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.exercises_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (R.id.menu_delete_all == item.itemId) {
            viewModel.nukeExercisesTable()
        }
        return super.onOptionsItemSelected(item)
    }

    private val itemTouchHelperCallback =
        object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {

                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val rowToDelete = exerciseListAdapter.getExerciseList()[viewHolder.adapterPosition]
                viewModel.deleteExercise(rowToDelete)
                Toast.makeText(
                    viewHolder.itemView.context,
                    "${rowToDelete.exerciseName} deleted",
                    Toast.LENGTH_SHORT
                ).show()
                //TODO Add an undo?
            }
        }
}
