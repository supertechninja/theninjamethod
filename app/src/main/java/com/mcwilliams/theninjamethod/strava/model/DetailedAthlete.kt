package com.mcwilliams.theninjamethod.strava.model

data class DetailedAthlete(
    val id: Long = 1,    // unique identifier of the athlete
    val resource_state: Int = 2, // state, indicates level of detail. Possible values: 1 -> "meta", 2 -> "summary", 3 -> "detail"
    val firstname: String = "",    // athlete's first name.
    val lastname: String = "", // athlete's last name.
    val profile_medium: String = "", // to a 62x62 pixel profile picture.
    val profile: String = "",    // to a 124x124 pixel profile picture.
    val city: String = "",    // The athlete's city.
    val state: String = "", // The athlete's state or geographical region.
    val country: String = "", // The athlete's country.
    val sex: String = "", // The athlete's sex. May take one of the following values: M, F
    val premium: Boolean = true, // Deprecated. Use summit field instead. Whether the athlete has any Summit subscription.
    val summit: Boolean = true,  // Whether the athlete has any Summit subscription.
    val created_at: DateTime = DateTime(), // The time at which the athlete was created.
    val updated_at: DateTime = DateTime(), // The time at which the athlete was last updated.
    val follower_count: Int = 3, // The athlete's follower count.
    val friend_count: Int = 4, // The athlete's friend count.
    val measurement_preference: String = "", // The athlete's preferred unit system. May take one of the following values: feet, meters
    val ftp: Int = 5, // The athlete's FTP (Functional Threshold Power).
    val weight: Float = 6F, // The athlete's weight.
    val clubs: SummaryClub = SummaryClub(), // The athlete's clubs.
    val bikes: SummaryGear = SummaryGear(), // The athlete's bikes.
    val shoes: SummaryGear = SummaryGear() // The athlete's shoes.
)