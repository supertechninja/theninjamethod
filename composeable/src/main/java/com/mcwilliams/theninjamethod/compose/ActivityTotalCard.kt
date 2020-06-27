package com.mcwilliams.theninjamethod.compose

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.compose.Composable
import androidx.compose.Recomposer
import androidx.ui.core.Modifier
import androidx.ui.core.setContent
import androidx.ui.foundation.Text
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.layout.*
import androidx.ui.material.Card
import androidx.ui.material.MaterialTheme
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import com.mcwilliams.theninjamethod.compose.ui.ComposeTheme
import com.mcwilliams.theninjamethod.strava.model.strava.athlete.ActivityTotal

class ActivityTotalCard @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    activityTotal: ActivityTotal
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        setContent(Recomposer.current()) {
            createTotalCard("All Ride Totals", activityTotal)
        }
    }


}

@Composable
private fun createTotalCard(title: String, activityTotal: ActivityTotal) {
    Card(modifier = Modifier.preferredWidth(400.dp).preferredHeight(300.dp), shape = RoundedCornerShape(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.h6, modifier = Modifier.padding(bottom = 8.dp))
            activityTotal.let {
                createRow("Count", it.count.toString())
                createRow("Distance", it.distance.toString())
                createRow("Moving time", it.moving_time.toString())
                createRow("Elapsed time", it.elapsed_time.toString())
                createRow("Elevation gain", it.elevation_gain.toString())
            }
        }
    }
}

@Composable
fun createRow(label: String, value: String) {
    Row(modifier = Modifier.padding(bottom = 6.dp)) {
        Text(text = label)
        Spacer(modifier = Modifier.preferredWidth(20.dp))
        Text(text = value)
    }
}

@Preview("Card Preview", showBackground = true)
@Composable
fun cardPreview() {
    ComposeTheme(darkTheme = true) {
        createTotalCard("All Ride Totals", activityTotal = ActivityTotal(0, 0, 0, 0, 0))
    }
}

