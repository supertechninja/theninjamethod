package com.mcwilliams.theninjamethod.strava.model.athlete

data class Bike(
    val distance: Int,
    val id: String,
    val name: String,
    val primary: Boolean,
    val resource_state: Int
)