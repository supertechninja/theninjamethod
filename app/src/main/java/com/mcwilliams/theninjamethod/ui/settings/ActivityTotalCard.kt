package com.mcwilliams.theninjamethod.ui.settings

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.marginBottom
import com.mcwilliams.theninjamethod.strava.model.athlete.ActivityTotal

class ActivityTotalCard(
    private val _context: Context, private val title: String, private val at: ActivityTotal,
    private val attrs: AttributeSet?,
    private val defStyleAttr: Int
) : CardView(_context, attrs, defStyleAttr) {

    init {
        val ll = LinearLayout(_context, attrs, defStyleAttr)
        ll.orientation = LinearLayout.VERTICAL
        val textView = TextView(context, attrs, defStyleAttr)
        textView.setTypeface(null, Typeface.BOLD)
        textView.text = title
        ll.addView(textView)
        addView(ll)

        ll.addView(createRow("Count", at.count))
        ll.addView(createRow("Distance", at.distance))
        ll.addView(createRow("Elapsed Time", at.elapsed_time))
        ll.addView(createRow("Elevation Gain", at.elevation_gain))
        ll.addView(createRow("Moving Time", at.moving_time))
        ll.setPadding(50,0,0,50)
    }

    private fun createRow(label: String, value: Int): View? {
        val row = LinearLayout(_context, attrs, defStyleAttr)
        row.orientation = LinearLayout.HORIZONTAL

        row.addView(getTV(label))
        row.addView(getTV(value.toString()))
        return row
    }

    private fun getTV(label: String): TextView {
        val tv = TextView(context, attrs, defStyleAttr)
        tv.text = label
        return tv
    }

}

