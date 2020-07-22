package com.mcwilliams.theninjamethod.ui.routines

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
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

        val chipGroup = view.findViewById<ChipGroup>(R.id.chips_group)
        val chips = mutableListOf("All", "Beginner", "Medium", "Hard", "Custom")
        createChips(chipGroup, chips)
        chipGroup.setOnCheckedChangeListener { group, checkedId ->
            val chip: Chip = chipGroup.findViewById(checkedId)
            val sortValue = chip.text
            sortRoutines(sortValue)
        }

        val routineList = view.findViewById<RecyclerView>(R.id.routines_list)
        routineList.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        routineList.adapter = routinesAdapter

        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)

        viewModel.workout.observe(viewLifecycleOwner, Observer {
            Log.d("TAG", "onViewCreated: ${it.size}")
            routinesAdapter.updateRoutines(it.toMutableList())
            progressBar.visibility = View.GONE
        })

    }

    private fun sortRoutines(sortValue: CharSequence?) {
//        routinesAdapter.getRoutinesList().find {  }
    }

    fun createChips(chipsGroup: ChipGroup, chipsToCreate: List<String>) {
        chipsToCreate.forEach {
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
            if (it == "All") {
                chip.isChecked = true
            } else {
                chip.isCheckable = false
            }
            chip.setTextColor(
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        android.R.color.black
                    )
                )
            )
            chip.text = it
            chipsGroup.addView(chip)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return super.onOptionsItemSelected(item)
    }

}

