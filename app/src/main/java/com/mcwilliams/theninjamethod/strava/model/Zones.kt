package com.mcwilliams.theninjamethod.strava.model

data class Zones(
    val heart_rate: HeartRateZoneRanges, // An instance of HeartRateZoneRanges.
    val power: PowerZoneRanges // An instance of PowerZoneRanges.
)