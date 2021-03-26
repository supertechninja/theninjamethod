package com.mcwilliams.theninjamethod.ui.activity.stravadetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.button.MaterialButton
import com.mcwilliams.theninjamethod.R
import com.mcwilliams.theninjamethod.strava.model.activitydetail.StravaActivityDetail
import com.muddzdev.quickshot.QuickShot
import java.time.LocalDateTime
import kotlin.math.roundToInt

class ShareStravaWorkoutFragment : Fragment(), QuickShot.QuickShotListener {
    lateinit var workout: StravaActivityDetail
    lateinit var rootView: ConstraintLayout
    lateinit var composeView: ComposeView
    private val viewModel: ShareStravaWorkoutViewModel by viewModels()

    @ExperimentalAnimationApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        workout = arguments?.getSerializable("workout") as StravaActivityDetail

        return inflater.inflate(R.layout.share_strava_workout_image, container, false).apply {
            findViewById<ComposeView>(R.id.compose_view).setContent {
                ShareStravaDetails(workout, viewModel)
            }

            findViewById<ComposeView>(R.id.compose_toggle_view).setContent {
                ImageToggles(viewModel)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        composeView = view.findViewById(R.id.compose_view)

        val btnShare = view.findViewById<MaterialButton>(R.id.btnShare)
        btnShare.setOnClickListener {
            share()
        }
    }

//    fun getMapUrl(polyline: String): String {
//        return "https://maps.googleapis.com/maps/api/staticmap?size=600x300&scale=2&maptype=roadmap&path=enc:${polyline}&key=${BuildConfig.MAPS_API_KEY}"
//    }

    private fun share() {
        QuickShot.of(composeView).setResultListener(this)
            .setFilename("ninja-method-${LocalDateTime.now()}")
            .setPath("NinjaMethod")
            .toJPG()
            .save();

    }

    override fun onQuickShotSuccess(path: String?) {
        Log.d("TAG", "onQuickShotSuccess: $path")
        Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
    }

    override fun onQuickShotFailed(path: String?) {
        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
    }
}

@ExperimentalAnimationApi
@Composable
fun ShareStravaDetails(workout: StravaActivityDetail, viewModel: ShareStravaWorkoutViewModel) {
    val cardColor by viewModel.cardColor.observeAsState(initial = Color.White)
    val animatedCardColor by animateColorAsState(targetValue = cardColor)

    Surface(
        shape = RectangleShape,
        color = animatedCardColor,
        modifier = Modifier
            .wrapContentHeight()
    ) {
        ConstraintLayout() {
            val (runstats, logo) = createRefs()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(runstats) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = workout.name,
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.padding(bottom = 8.dp, top = 16.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    StackedRunDetail(label = "Distance", value = workout.miles)
                    StackedRunDetail(label = "Duration", value = workout.duration)
                    StackedRunDetail(label = "Calories", value = "${workout.calories.toInt()}")
                }

                val showSplits by viewModel.showSplits.observeAsState()

                AnimatedVisibility(visible = showSplits!!) {
                    Column() {
                        Text(
                            text = "Splits",
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp)
                        )

                        workout.splits_standard?.let { splits ->
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
                                if (split.distance > 10) {
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
                    }
                }
            }

            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 4.dp)
                    .constrainAs(logo) {
                        end.linkTo(parent.end, 8.dp)
                        top.linkTo(runstats.bottom, 8.dp)
                    }, horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.ic_launcher_foreground),
                    contentDescription = "logo",
                    modifier = Modifier
                        .size(80.dp)
                )

                Text(
                    text = "Ninja Method",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.offset(y = -10.dp)
                )
            }
        }
    }
}

@Composable
fun ImageToggles(viewModel: ShareStravaWorkoutViewModel) {
    val showSplits by viewModel.showSplits.observeAsState()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            Switch(
                checked = showSplits!!,
                modifier = Modifier.padding(horizontal = 8.dp),
                onCheckedChange = {
                    viewModel.toggleSplits()
                })
            Text(
                text = "${if (showSplits!!) "Hide" else "Show"} Splits",
                color = Color.White
            )
        }
        val scrollState = rememberScrollState()
        val colors =
            listOf(
                Color.Blue,
                Color.DarkGray,
                Color.Green,
                Color.Magenta,
                Color.Yellow,
                Color.Cyan,
                Color.Red,
                Color.LightGray
            )

        val selectedColor by viewModel.cardColor.observeAsState()

        Row(
            modifier = Modifier
                .horizontalScroll(scrollState)
        ) {
            colors.forEach {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(4.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .size(60.dp)
                            .clickable {
                                viewModel.setColor(it)
                            },
                        shape = CircleShape,
                        border = BorderStroke(2.dp, Color.White),
                        color = it
                    ) {

                    }

                    if (it == selectedColor) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            tint = Color.Black,
                            contentDescription = "Selected"
                        )
                    }
                }

            }
        }

    }

}

@Composable
fun StackedRunDetail(label: String, value: String) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}

@Composable
fun RunDetail(label: String, width: Dp, textStyle: TextStyle) {
    Column(
        modifier = Modifier
            .width(width = width)
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = label,
            style = textStyle,
            modifier = Modifier.padding(bottom = 2.dp)
        )
    }
}