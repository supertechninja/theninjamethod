package com.mcwilliams.theninjamethod.ui.activity.stravadetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.google.accompanist.coil.CoilImage
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.strava.model.activitydetail.StravaActivityDetail
import com.mcwilliams.theninjamethod.utils.extensions.getTimeFloat
import com.robinhood.spark.SparkAdapter
import com.robinhood.spark.SparkView
import kotlin.math.roundToInt

fun getMapUrl(polyline: String): String {
    return "https://maps.googleapis.com/maps/api/staticmap?size=700x350&scale=2&maptype=roadmap&path=enc:${polyline}&key=AIzaSyBWhwSFZ1aFOyxGN057wR_4wMA3QMyLT9I"
}

@Composable
fun StravaDetailContent(
    viewModel: StravaDetailViewModel,
    workoutId: String,
    navController: NavController,
    paddingValues: PaddingValues
) {
    val detailActivity by viewModel.detailedActivity.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(true)
    viewModel.getDetailedActivities(workoutId.toLong())

    if (isLoading) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(100.dp)
            )
        }
    } else {
        val scrollState = rememberScrollState()
        detailActivity?.let { stravaDetailActivity ->
            Box(modifier = Modifier.padding(paddingValues = paddingValues)) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .verticalScroll(scrollState)
                ) {
                    Text(
                        text = stravaDetailActivity.name,
                        style = MaterialTheme.typography.h4,
                        color = Color.White,
                    )

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        Text(
                            text = stravaDetailActivity.formattedDate,
                            style = MaterialTheme.typography.body2,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
                        )

                        if (stravaDetailActivity.has_heartrate) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_heart_icon),
                                    contentDescription = "",
                                    tint = Color.Red,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .padding(end = 4.dp)
                                )
                                Text(
                                    text = "${stravaDetailActivity.average_heartrate} bpm",
                                    style = MaterialTheme.typography.body2,
                                    color = Color.White,
                                    modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
                                )
                            }
                        }
                    }
//
//                    Row(
//                        horizontalArrangement = Arrangement.SpaceEvenly,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(bottom = 16.dp)
//                    ) {
//                        StackedRunDetail(label = "Distance", value = stravaDetailActivity.miles)
//                        StackedRunDetail(label = "Duration", value = stravaDetailActivity.duration)
//                        StackedRunDetail(
//                            label = "Calories",
//                            value = "${stravaDetailActivity.calories.toInt()}"
//                        )
//                    }

                    StravaMapWithStats(stravaDetailActivity)

                    Text(
                        text = "Splits",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = Color.White
                    )

                    val splitsPaceArray: MutableList<Float> = mutableListOf()

                    stravaDetailActivity.splits_standard?.let { splits ->
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            RunDetail(
                                label = "MI",
                                width = 50.dp,
                                textStyle = MaterialTheme.typography.body1
                            )
                            RunDetail(
                                label = "Pace",
                                width = 100.dp,
                                textStyle = MaterialTheme.typography.body1
                            )
                            RunDetail(
                                label = "HR",
                                width = 50.dp,
                                textStyle = MaterialTheme.typography.body1
                            )
                        }

                        splits.forEach { split ->
                            if (split.distance > 50) {
                                splitsPaceArray.add(split.moving_time.getTimeFloat())

                                Row(
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    RunDetail(
                                        label = "${split.split}",
                                        width = 50.dp,
                                        textStyle = MaterialTheme.typography.body2
                                    )
                                    RunDetail(
                                        label = "${split.moving_time / 60}m ${split.moving_time % 60}s",
                                        width = 100.dp,
                                        textStyle = MaterialTheme.typography.body2
                                    )
                                    RunDetail(
                                        label = "${split.average_heartrate.roundToInt()}",
                                        width = 50.dp,
                                        textStyle = MaterialTheme.typography.body2
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.requiredHeight(32.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        var splitText by remember { mutableStateOf("") }

                        AndroidView(
                            factory = { context ->
                                val sparkview = SparkView(context)
                                sparkview.adapter = MyAdapter(splitsPaceArray.toFloatArray())
                                sparkview.isScrubEnabled = true
                                sparkview.lineColor = context.getColor(android.R.color.white)
                                sparkview.setScrubListener { value ->
                                    splitText = value?.toString() ?: ""
                                }
                                sparkview
                            }, modifier = Modifier
                                .fillMaxWidth()
                                .requiredHeight(70.dp)
                        )

                        if (splitText.isNotEmpty()) {
                            Text(
                                text = "Split: $splitText",
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(end = 8.dp),
                                color = Color.White
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 16.dp, end = 16.dp),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    FloatingActionButton(
                        onClick = {
                            val bundle = bundleOf(
                                "workout" to stravaDetailActivity
                            )
                            navController.navigate(R.id.navigate_to_share_strava_workout, bundle)
                        },
                        backgroundColor = MaterialTheme.colors.primary,
                        contentColor = MaterialTheme.colors.onPrimary
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share Workout"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StravaMapWithStats(stravaDetailActivity: StravaActivityDetail) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        stravaDetailActivity.map?.let {
            CoilImage(
                data = getMapUrl(it.polyline),
                contentDescription = "",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                loading = {

                }
            )
        }

        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(bottom = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${stravaDetailActivity.miles}",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = Color.Black
                )
            }
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${stravaDetailActivity.duration}",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = Color.Black
                )
            }
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${stravaDetailActivity.calories} cal",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = Color.Black
                )
            }
        }
    }
}

class MyAdapter(private val yData: FloatArray) : SparkAdapter() {
    override fun getCount(): Int {
        return yData.size
    }

    override fun getItem(index: Int): Any {
        return yData[index]
    }

    override fun getY(index: Int): Float {
        return yData[index]
    }
}


