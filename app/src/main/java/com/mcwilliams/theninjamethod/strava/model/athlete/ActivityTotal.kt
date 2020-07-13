package com.mcwilliams.theninjamethod.strava.model.athlete

data class ActivityTotal(
    val count: Int,
    val distance: Int,
    val elapsed_time: Int,
    val elevation_gain: Int,
    val moving_time: Int
)