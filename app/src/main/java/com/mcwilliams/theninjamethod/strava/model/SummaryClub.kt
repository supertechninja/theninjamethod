package com.mcwilliams.theninjamethod.strava.model

data class SummaryClub(
    val id: Long = 0,     // The club's unique identifier.
    val resource_state: Int = 1,    // Resource state, indicates level of detail. Possible values: 1 -> "meta", 2 -> "summary", 3 -> "detail"
    val name: String = "",    // The club's name.
    val profile_medium: String = "",     // URL to a 60x60 pixel profile picture.
    val cover_photo: String = "",    // URL to a ~1185x580 pixel cover photo.
    val cover_photo_small: String = "",     // URL to a ~360x176 pixel cover photo.
    val sport_type: String = "",     // May take one of the following values: cycling, running, triathlon, other
    val city: String = "",    // The club's city.
    val state: String = "",    // The club's state or geographical region.
    val country: String = "",    // The club's country.
    val private: Boolean = true,    // Whether the club is private.
    val member_count: Int = 2,    // The club's member count.
    val featured: Boolean = true,    // Whether the club is featured or not.
    val verified: Boolean = true,    // Whether the club is verified or not.
    val url: String = ""    // The club's vanity URL.
)
