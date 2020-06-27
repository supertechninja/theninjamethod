package com.mcwilliams.theninjamethod.compose

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.compose.Composable
import androidx.compose.Recomposer
import androidx.ui.core.Alignment
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
import com.mcwilliams.theninjamethod.strava.model.activites.Athlete
import com.mcwilliams.theninjamethod.strava.model.strava.athlete.ActivityTotal
import com.mcwilliams.theninjamethod.strava.model.strava.athlete.Bike
import com.mcwilliams.theninjamethod.strava.model.strava.athlete.Shoe
import com.mcwilliams.theninjamethod.strava.model.strava.athlete.StravaAthlete
import dev.chrisbanes.accompanist.coil.CoilImage

class Profile @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    athlete: StravaAthlete
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        setContent(Recomposer.current()) {
            ProfileView(athlete)
        }
    }


}

@Composable
fun ProfileView(athlete: StravaAthlete) {
    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth().fillMaxHeight(),
        horizontalGravity = Alignment.CenterHorizontally
    ) {
        CoilImage(data = athlete.profile_medium)
        Text(
            text = "${athlete.firstname} ${athlete.lastname}",
            style = MaterialTheme.typography.h6
        )
        Text(text = "${athlete.city}, ${athlete.state}", style = MaterialTheme.typography.body1)
    }

}

@Preview("Profile Preview", showBackground = true)
@Composable
fun profilePreview() {
    ComposeTheme(darkTheme = true) {
        ProfileView(
            StravaAthlete(
                2,
                2,
                listOf(Bike(20, "", "name", true, 0)),
                "SA",
                clubs = listOf("A"),
                country = "US",
                created_at = "2398",
                date_preference = "mmddyyyy",
                firstname = "Bob",
                follower = "",
                follower_count = 2,
                friend = "",
                friend_count = 4,
                ftp = "",
                id = 2L,
                lastname = "Fred",
                measurement_preference = "imperial",
                mutual_friend_count = 1,
                premium = false,
                profile = "https",
                profile_medium = "https://image",
                resource_state = 0,
                sex = "male",
                shoes = listOf(Shoe(3, "sdf", "sdf", true, 0)),
                state = "TX",
                updated_at = "2398",
                username = "fred.bob",
                weight = 123
            )
        )
    }
}

