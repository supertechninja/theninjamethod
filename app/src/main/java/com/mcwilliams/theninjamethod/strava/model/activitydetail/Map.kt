package com.mcwilliams.theninjamethod.strava.model.activitydetail

data class Map(
    val id: String,
    val polyline: String,
    val resource_state: Int,
    val summary_polyline: String?
)