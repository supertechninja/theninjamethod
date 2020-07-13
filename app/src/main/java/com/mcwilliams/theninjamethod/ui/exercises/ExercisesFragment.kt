package com.mcwilliams.theninjamethod.ui.exercises

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.databinding.FragmentHomeBinding
import com.mcwilliams.theninjamethod.ui.exercises.db.Exercise
import com.mcwilliams.theninjamethod.ui.exercises.model.ExerciseType
import com.mcwilliams.theninjamethod.ui.exercises.viewmodel.ExerciseListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*


@AndroidEntryPoint
class ExercisesFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: ExerciseListViewModel by viewModels()
    private val exerciseListAdapter: ExerciseListAdapter = ExerciseListAdapter()
    private var exerciseList: MutableList<Exercise> = mutableListOf()
    var didSaveMasterList: Boolean = false

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
            if (!didSaveMasterList) {
                exerciseList = it.toMutableList()
                didSaveMasterList = true
            }
            exerciseListAdapter.updatePostList(it.toMutableList())
            binding.progressBar.visibility = View.GONE
        })

        ExerciseType.values().forEach {
            val chip = Chip(requireContext())
            val chipDrawable = ChipDrawable.createFromAttributes(
                requireContext(),
                null,
                0,
                R.style.Widget_MaterialComponents_Chip_Filter
            )
            chip.setChipDrawable(chipDrawable)
            chip.chipBackgroundColor = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.color_secondary
                )
            )
            chip.setTextColor(
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        android.R.color.black
                    )
                )
            )
            chip.text = it.exerciseType
            binding.chipsGroup.addView(chip)
        }

        binding.chipsGroup.forEach { child ->
            (child as? Chip)?.setOnCheckedChangeListener { _, _ ->
                registerFilterChanged()
            }
        }

        binding.addExercise.setOnClickListener {
            AddExerciseDialog().show(parentFragmentManager, "TAG")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.exercises_fragment_menu, menu)

        val searchViewItem = menu.findItem(R.id.menu_search);
        val searchView: SearchView = searchViewItem.actionView as SearchView

        // attach setOnQueryTextListener
        // to search view defined above
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                exerciseListAdapter.getExerciseList().filter { it.exerciseName == query }
                exerciseListAdapter.notifyDataSetChanged()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
//                Log.i(TAG,"Llego al querytextchange")
//                adapter.getFilter().filter(newText);
                return true
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (R.id.menu_delete_all == item.itemId) {
            viewModel.nukeExercisesTable()
        } else if (R.id.menu_sort == item.itemId) {
            showFilterChips()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showFilterChips() {
        // Prepare the View for the animation
        chips_group.visibility = View.VISIBLE;
        chips_group.alpha = 0.0f;
        chips_group.animate().setDuration(300)
            .translationY(chips_group.height.toFloat())
            .alpha(1.0f)
            .setListener(null);
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

    private fun registerFilterChanged() {
        val ids = binding.chipsGroup.checkedChipIds
        val titles = mutableListOf<CharSequence>()

        ids.forEach { id ->
            titles.add(binding.chipsGroup.findViewById<Chip>(id).text)
        }

        if (titles.isNotEmpty()) {
            val allFilteredExerciseList: MutableList<List<Exercise>> = mutableListOf()
            titles.forEach { exerciseChip: CharSequence ->
                val exerciseList =
                    exerciseList.filter { it.definedExerciseType!!.name == exerciseChip }
                allFilteredExerciseList.add(exerciseList)
            }

            exerciseListAdapter.updatePostList(allFilteredExerciseList.flatten().toMutableList())
        } else {
            exerciseListAdapter.updatePostList(exerciseList)
        }

    }
}
