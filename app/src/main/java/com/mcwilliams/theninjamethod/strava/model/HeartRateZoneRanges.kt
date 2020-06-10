package com.mcwilliams.theninjamethod.strava.model

data class HeartRateZoneRanges(
    val custom_zones: Boolean, // Whether the athlete has set their own custom heart rate zones
    val zones: ZoneRanges // An instance of ZoneRanges.
)
