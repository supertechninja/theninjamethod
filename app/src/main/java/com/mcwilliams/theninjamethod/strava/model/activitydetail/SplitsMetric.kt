package com.mcwilliams.theninjamethod.strava.model.activitydetail

data class SplitsMetric(
    val average_speed: Double,
    val distance: Double,
    val elapsed_time: Int,
    val elevation_difference: Double,
    val moving_time: Int,
    val pace_zone: Int,
    val split: Int
)