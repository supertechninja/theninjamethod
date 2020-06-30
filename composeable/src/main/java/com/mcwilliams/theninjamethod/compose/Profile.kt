package com.mcwilliams.theninjamethod.compose

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.compose.*
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.core.setContent
import androidx.ui.foundation.Image
import androidx.ui.foundation.Text
import androidx.ui.foundation.VerticalScroller
import androidx.ui.layout.*
import androidx.ui.material.Divider
import androidx.ui.material.MaterialTheme
import androidx.ui.res.loadImageResource
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import com.mcwilliams.theninjamethod.compose.ui.ComposeTheme
import com.mcwilliams.theninjamethod.strava.model.strava.athlete.ActivityTotal
import dev.chrisbanes.accompanist.coil.CoilImage

class Profile @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    username: String,
    name: String,
    location: String,
    friendCount: Int,
    followerCount: Int,
    premium: Boolean,
    profilePic: String,
    totals: List<ActivityTotalCardModel>?
) : LinearLayout(context, attrs, defStyleAttr) {
    val totalCardModelList = state { totals }

    init {
        setContent(Recomposer.current()) {
            ProfileView(
                username,
                name,
                location,
                friendCount,
                followerCount,
                premium,
                profilePic,
                totalCardModelList
            )
        }
    }

}

@Composable
fun ProfileView(
    username: String,
    name: String,
    location: String,
    friendCount: Int,
    followerCount: Int,
    premium: Boolean,
    profilePic: String,
    activityTotals: MutableState<List<ActivityTotalCardModel>?> = mutableStateOf(null),
    isPreview: Boolean = false
) {
    val totals = state{activityTotals}
    VerticalScroller {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth().fillMaxHeight(),
            horizontalGravity = Alignment.CenterHorizontally
        ) {
            if (isPreview) {
                val image = loadImageResource(R.drawable.android)
                image.resource.resource?.let {
                    Image(asset = it)
                }
            } else {
                CoilImage(data = profilePic)
            }
            Text(text = name, style = MaterialTheme.typography.h6)
            Row {
                Text(text = location, style = MaterialTheme.typography.body1)
            }
            for (card in activityTotals.value ?: emptyList()) {
                Divider()
                ActivityTotalCard(card = card)
            }
        }
    }

}


@Preview("Profile Preview", showBackground = true)
@Composable
fun profilePreview() {
    ComposeTheme(darkTheme = true) {
        ProfileView(
            username = "bobTheBuilder",
            name = "Bob Builder",
            location = "Austin, TX",
            friendCount = 2,
            followerCount = 5,
            premium = true,
            profilePic = "some url",
            isPreview = true
        )
        mutableStateOf(
            listOfNotNull(
                ActivityTotalCardModel(
                    "All Ride Totals",
                    activityTotal = ActivityTotal(
                        0,
                        0,
                        0,
                        0,
                        0
                    )
                ),
                ActivityTotalCardModel(
                    title = "All Swim Totals",
                    activityTotal = ActivityTotal(
                        12,
                        13,
                        14,
                        15,
                        16
                    )
                )
            )
        )
    }
}

