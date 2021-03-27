package com.mcwilliams.theninjamethod.ui.activity.stravadetail

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.composethemeadapter.MdcTheme
import com.google.android.material.textview.MaterialTextView
import com.mcwilliams.data.workoutdb.SimpleWorkout
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.strava.model.activitydetail.StravaActivityDetail
import com.mcwilliams.theninjamethod.theme.TheNinjaMethodTheme
import com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist.ActivityBodyContent
import com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist.ActivityContentScaffold
import com.mcwilliams.theninjamethod.utils.extensions.getTimeFloat
import com.mcwilliams.theninjamethod.utils.extensions.getTimeString
import com.robinhood.spark.SparkAdapter
import com.robinhood.spark.SparkView
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class StravaWorkoutDetailFragment : Fragment() {
    lateinit var workout: SimpleWorkout
    private val viewModel: StravaDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        workout = arguments?.getSerializable("workout") as SimpleWorkout
        return ComposeView(context = requireContext()).apply {
            setContent {
                TheNinjaMethodTheme {
                    StravaDetailContent(
                        viewModel = viewModel,
                        workout = workout,
                        findNavController()
                    )
                }
            }
        }
    }

//    fun getMapUrl(polyline: String): String {
//        return "https://maps.googleapis.com/maps/api/staticmap?size=700x350&scale=2&maptype=roadmap&path=enc:${polyline}&key=${BuildConfig.MAPS_API_KEY}"
//    }
}

@Composable
fun StravaDetailContent(
    viewModel: StravaDetailViewModel,
    workout: SimpleWorkout,
    navController: NavController
) {
    val detailActivity by viewModel.detailedActivity.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(true)
    viewModel.getDetailedActivities(workout.id)

    if (isLoading) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(100.dp)
            )
        }
    } else {
        detailActivity?.let { stravaDetailActivity ->
            Box() {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp)
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
                            .padding(bottom = 32.dp)
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
                                    modifier = Modifier.size(20.dp).padding(end = 4.dp)
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

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        StackedRunDetail(label = "Distance", value = stravaDetailActivity.miles)
                        StackedRunDetail(label = "Duration", value = stravaDetailActivity.duration)
                        StackedRunDetail(
                            label = "Calories",
                            value = "${stravaDetailActivity.calories.toInt()}"
                        )
                    }

                    Text(
                        text = "Splits",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(vertical = 8.dp),
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
                                textStyle = MaterialTheme.typography.h6
                            )
                            RunDetail(
                                label = "Pace",
                                width = 100.dp,
                                textStyle = MaterialTheme.typography.h6
                            )
                            RunDetail(
                                label = "HR",
                                width = 50.dp,
                                textStyle = MaterialTheme.typography.h6
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
                                        textStyle = MaterialTheme.typography.body1
                                    )
                                    RunDetail(
                                        label = "${split.moving_time / 60}m ${split.moving_time % 60}s",
                                        width = 100.dp,
                                        textStyle = MaterialTheme.typography.body1
                                    )
                                    RunDetail(
                                        label = "${split.average_heartrate.roundToInt()}",
                                        width = 50.dp,
                                        textStyle = MaterialTheme.typography.body1
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
                                .requiredHeight(100.dp)
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


