package com.mcwilliams.theninjamethod.utils

//package com.mcwilliams.theninjamethod.utils
//
//import android.graphics.Canvas
//import android.graphics.Color
//import android.graphics.drawable.ColorDrawable
//import android.graphics.drawable.Drawable
//import androidx.core.content.ContextCompat
//import androidx.recyclerview.widget.ItemTouchHelper
//import androidx.recyclerview.widget.RecyclerView
//import com.mcwilliams.theninjamethod.R
//import com.mcwilliams.theninjamethod.ui.home.ExerciseListAdapter
//import com.mcwilliams.theninjamethod.ui.home.viewmodel.ExerciseListViewModel
//
//
//class SwipeToDeleteCallback(
//    adapter: ExerciseListAdapter,
//    exerciseListViewModel: ExerciseListViewModel
//) :
//    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
//    private val mAdapter: ExerciseListAdapter = adapter
//    private val viewModel: ExerciseListViewModel = exerciseListViewModel
//    private val icon: Drawable?
//    private val background: ColorDrawable
//    override fun onMove(
//        recyclerView: RecyclerView,
//        viewHolder: RecyclerView.ViewHolder,
//        target: RecyclerView.ViewHolder
//    ): Boolean { // used for up and down movements
//        return false
//    }
//
//    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//        val position = viewHolder.adapterPosition
////        viewModel.deleteExercise(position)
////        mAdapter.deleteItem(position)
//    }
//
//    override fun onChildDraw(
//        c: Canvas,
//        recyclerView: RecyclerView,
//        viewHolder: RecyclerView.ViewHolder,
//        dX: Float,
//        dY: Float,
//        actionState: Int,
//        isCurrentlyActive: Boolean
//    ) {
//        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
//        val itemView = viewHolder.itemView
//        val backgroundCornerOffset =
//            20 //so background is behind the rounded corners of itemView
//        val iconMargin = (itemView.height - icon!!.intrinsicHeight) / 2
//        val iconTop =
//            itemView.top + (itemView.height - icon.intrinsicHeight) / 2
//        val iconBottom = iconTop + icon.intrinsicHeight
//        if (dX > 0) { // Swiping to the right
//            val iconLeft = itemView.left + iconMargin + icon.intrinsicWidth
//            val iconRight = itemView.left + iconMargin
//            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
//            background.setBounds(
//                itemView.left, itemView.top,
//                itemView.left + dX.toInt() + backgroundCornerOffset, itemView.bottom
//            )
//        } else if (dX < 0) { // Swiping to the left
//            val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
//            val iconRight = itemView.right - iconMargin
//            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
//            background.setBounds(
//                itemView.right + dX.toInt() - backgroundCornerOffset,
//                itemView.top, itemView.right, itemView.bottom
//            )
//        } else { // view is unSwiped
//            background.setBounds(0, 0, 0, 0)
//        }
//        background.draw(c)
//        icon.draw(c)
//    }
//
//    init {
//        icon = ContextCompat.getDrawable(
////            mAdapter.getContext(),
//            R.drawable.ic_delete
//        )
//        background = ColorDrawable(Color.RED)
//    }
//}