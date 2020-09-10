package com.mcwilliams.theninjamethod.ui.exercises

import android.os.Bundle
import android.view.*
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.ListItem
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.theme.TheNinjaMethodTheme
import com.mcwilliams.theninjamethod.ui.exercises.viewmodel.ExerciseListViewModel
import dagger.hilt.android.AndroidEntryPoint


@ExperimentalLayout
@ExperimentalMaterialApi
@AndroidEntryPoint
class ExercisesFragment : Fragment() {
    private val viewModel: ExerciseListViewModel by viewModels()
    var didSaveMasterList: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        return ComposeView(context = requireContext()).apply {
            setContent {
                TheNinjaMethodTheme {
                    ExerciseList(viewModel)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.showDialog.observe(viewLifecycleOwner, {
            if (it) {
                AddExerciseDialog().show(parentFragmentManager, "TAG")
            }
        })
    }

//
//        chipsGroup = view.findViewById<ChipGroup>(R.id.chips_group)
//        ExerciseType.values().forEach {
//            val chip = Chip(requireContext())
//            val chipDrawable = ChipDrawable.createFromAttributes(
//                requireContext(),
//                null,
//                0,
//                R.style.Widget_MaterialComponents_Chip_Filter
//            )
//            chip.setChipDrawable(chipDrawable)
//            chip.chipBackgroundColor = ColorStateList.valueOf(
//                ContextCompat.getColor(
//                    requireContext(),
//                    R.color.color_secondary
//                )
//            )
//            chip.setTextColor(
//                ColorStateList.valueOf(
//                    ContextCompat.getColor(
//                        requireContext(),
//                        android.R.color.black
//                    )
//                )
//            )
//            chip.text = it.exerciseType
//            chipsGroup.addView(chip)
//        }
//
//        chipsGroup.forEach { child ->
//            (child as? Chip)?.setOnCheckedChangeListener { _, _ ->
//                registerFilterChanged()
//            }
//        }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.exercises_fragment_menu, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (R.id.menu_delete_all == item.itemId) {
            viewModel.nukeExercisesTable()
        } else if (R.id.menu_sort == item.itemId) {
            viewModel._showChipFilters.postValue(!viewModel.showChipFilters.value!!)
        }
        return super.onOptionsItemSelected(item)
    }

//    private fun showFilterChips() {
//        // Prepare the View for the animation
//        chipsGroup.visibility = View.VISIBLE;
//        chipsGroup.alpha = 0.0f;
//        chipsGroup.animate().setDuration(300)
//            .translationY(chipsGroup.height.toFloat())
//            .alpha(1.0f)
//            .setListener(null);
//    }
//
//    private fun registerFilterChanged() {
//        val ids = chipsGroup.checkedChipIds
//        val titles = mutableListOf<CharSequence>()
//
//        ids.forEach { id ->
//            titles.add(chipsGroup.findViewById<Chip>(id).text)
//        }
//
//        if (titles.isNotEmpty()) {
//            val allFilteredExerciseList: MutableList<List<DbExercise>> = mutableListOf()
//            titles.forEach { exerciseChip: CharSequence ->
//                val exerciseList =
//                    exerciseList.filter { it.definedExerciseType!!.name == exerciseChip }
//                allFilteredExerciseList.add(exerciseList)
//            }
//
//            exerciseListAdapter.updatePostList(allFilteredExerciseList.flatten().toMutableList())
//        } else {
//            exerciseListAdapter.updatePostList(exerciseList)
//        }
//
//    }
}

@ExperimentalLayout
@ExperimentalMaterialApi
@Composable
fun ExerciseList(viewModel: ExerciseListViewModel) {
    val exerciseList by viewModel.exerciseList.observeAsState()

    Scaffold(
        bodyContent = {
            Column {
                exerciseList?.let { exercises ->
                    LazyColumnFor(
                        items = exercises,
                        modifier = Modifier.fillMaxSize()
                    ) { exercise ->
                        ListItem(text = {
                            Text(text = exercise.exerciseName, color = Color.White)
                        })
//                    var delete by remember { mutableStateOf(false) }
//                    val dismissState = rememberDismissState(
//                        confirmStateChange = {
//                            if (it == DismissValue.DismissedToEnd) delete = !delete
//                            it != DismissValue.DismissedToEnd
//                        }
//                    )

//                    SwipeToDismiss(
//                        state = dismissState,
//                        modifier = Modifier.padding(vertical = 4.dp),
//                        directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
//                        dismissThresholds = { direction ->
//                            FractionalThreshold(if (direction == DismissDirection.StartToEnd) 0.25f else 0.5f)
//                        },
//                        background = {
//                            val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
//                            val color = animate(when (dismissState.targetValue) {
//                                DismissValue.Default -> Color.LightGray
//                                DismissValue.DismissedToEnd -> Color.Green
//                                DismissValue.DismissedToStart -> Color.Red
//                            })
//                            val gravity = when (direction) {
//                                DismissDirection.StartToEnd -> ContentGravity.CenterStart
//                                DismissDirection.EndToStart -> ContentGravity.CenterEnd
//                            }
//                            val icon = when (direction) {
//                                DismissDirection.StartToEnd -> Icons.Default.Done
//                                DismissDirection.EndToStart -> Icons.Default.Delete
//                            }
//                            val scale = animate(if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f)
//
//                            Box(
//                                modifier = Modifier.fillMaxSize(),
//                                backgroundColor = color,
//                                paddingStart = 20.dp,
//                                paddingEnd = 20.dp,
//                                gravity = gravity
//                            ) {
//                                Icon(icon, Modifier.drawLayer(scaleX = scale, scaleY = scale))
//                            }
//                        },
//                        dismissContent = {
//                            if (delete){
//                                viewModel.deleteExercise(exercise = exercise)
//                            } else {
//                                Row(modifier = Modifier.padding(16.dp, 16.dp).fillMaxWidth()) {
//                                    Text(text = exercise.exerciseName, color = Color.White)
//                                }
//                            }
//                        })
                    }
                }
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = {
                viewModel._showDialog.postValue(true)
//            AddExerciseDialog().show(ContextAmbient.current., "TAG")
            }, text = {
                Text("Add Exercise")
            })
        }
    )

}