package com.mcwilliams.app_ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.core.setContent
import androidx.ui.foundation.Icon
import androidx.ui.foundation.Text
import androidx.ui.foundation.VerticalScroller
import androidx.ui.layout.*
import androidx.ui.material.*
import androidx.ui.res.imageResource
import androidx.ui.res.vectorResource
import androidx.ui.text.style.TextOverflow
import androidx.ui.unit.dp
import com.mcwilliams.app_ui.ui.LetsComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val viewModel = ViewModelProvider(this).get(WorkoutListViewModel::class.java)

        setContent {
            LetsComposeTheme {
                Container()
            }
        }
    }
}

@Composable
fun Container() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Lets Compose") }
//                navigationIcon = {
//                    IconButton(onClick = { }) {
//                        Icon(vectorResource(R.drawable.ic_input_add))
//                    }
//                }
            )
        },
        bodyContent = {
            homeBody()
        },
        bottomBar = {
            BottomNavigation(
                content = {
                    listOf(
                        BottomNavigationItem(
                            selected = true,
                            text = { Text(text = "Activity") },
                            onSelected = {},
                            icon = {
                                Icon(
                                    asset = vectorResource(id = R.drawable.ic_running_icon),
                                    modifier = Modifier.width(24.dp).height(24.dp)
                                )
                            }
                        ),
                        BottomNavigationItem(
                            selected = false,
                            text = { Text(text = "Routines") },
                            onSelected = {},
                            icon = {
                                Icon(
                                    asset = imageResource(id = R.drawable.ic_routine_icon),
                                    modifier = Modifier.width(24.dp).height(24.dp)
                                )
                            }
                        ),
                        BottomNavigationItem(
                            selected = false,
                            text = { Text(text = "Exercises") },
                            onSelected = {},
                            icon = { Icon(asset = vectorResource(id = R.drawable.ic_exercises_icon)) }
                        ),
                        BottomNavigationItem(
                            selected = false,
                            text = { Text(text = "Settings") },
                            onSelected = {},
                            icon = { Icon(asset = vectorResource(id = R.drawable.ic_dashboard_black_24dp)) }
                        )
                    )
                }
            )
        }
    )
}

@Composable
fun homeBody() {
    VerticalScroller {
        for (x in 0..15) {
            Row(modifier = Modifier.fillMaxWidth().absolutePadding(16.dp, 8.dp, 16.dp, 8.dp)) {
                Card(
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    shape = MaterialTheme.shapes.medium,
                    elevation = 4.dp
                ) {
                    Row(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = x.toString(),
                            style = MaterialTheme.typography.h6,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

//
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    LetsComposeTheme {
////        Container(viewModel.workoutMapLiveData)
//    }
//}