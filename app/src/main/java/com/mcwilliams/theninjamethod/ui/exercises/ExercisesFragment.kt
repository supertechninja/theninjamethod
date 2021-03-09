package com.mcwilliams.theninjamethod.ui.exercises

import android.os.Bundle
import android.view.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.composethemeadapter.MdcTheme
import com.mcwilliams.data.exercisedb.DbExercise
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.theme.TheNinjaMethodTheme
import com.mcwilliams.theninjamethod.ui.exercises.viewmodel.ExerciseListViewModel
import dagger.hilt.android.AndroidEntryPoint


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
                MdcTheme {
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

@ExperimentalMaterialApi
@Composable
fun ExerciseList(viewModel: ExerciseListViewModel) {
    val exerciseList by viewModel.exerciseList.observeAsState()
//    var showDeleteDialog by remember { mutableStateOf(false) }
//    var exerciseToDelete = remember { MutableList<DbExercise>() }

    Scaffold(
        content = {
            Column {
                exerciseList?.let { exercises ->
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(exerciseList!!) { exercise ->
                            ListItem(text = {
                                Text(text = exercise.exerciseName, color = Color.White)
                            },
//                                modifier = Modifier.tou
//                            longPressGestureFilter {
////                            exerciseToDelete = exercise
////                            showDeleteDialog = true
//                            }
                            )

                        }
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

//    if (showDeleteDialog) {
//        val onDeleteExerciseClicked = {
//            viewModel.deleteExercise(exerciseToDelete!!)
//            showDeleteDialog = false
//            exerciseToDelete = null
//        }
//        val onCancelDeleteExerciseClicked = {
//            showDeleteDialog = false
//            exerciseToDelete = null
//        }
//        DeleteExerciseDialog(
//            exercise = exerciseToDelete!!,
//            onDeleteExerciseClicked,
//            onCancelDeleteExerciseClicked
//        )
//    }
}

@Composable
fun DeleteExerciseDialog(
    exercise: DbExercise,
    onDeleteExerciseClicked: () -> Unit,
    onCancelDeleteExerciseClicked: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text(text = "Delete Exercise") },
        text = {
            Text(
                text = "Are you sure you want to delete ${exercise.exerciseName}?"
            )
        },
        buttons = {
            TextButton(onClick = onCancelDeleteExerciseClicked) {
                Text(text = "No")
            }
            TextButton(onClick = onDeleteExerciseClicked) {
                Text(text = "Yes")
            }
        }
    )
}