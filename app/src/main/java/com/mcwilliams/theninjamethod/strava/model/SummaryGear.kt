package com.mcwilliams.theninjamethod.strava.model

data class SummaryGear(
    val id: String = "",    // The gear's unique identifier.
    val resource_state: Int = 0,// Resource state, indicates level of detail. Possible values: 2 -> "summary", 3 -> "detail"
    val primary: Boolean = true,   // Whether this gear's is the owner's default one.
    val name: String = "", // The gear's name.
    val distance: Float = 1F    // The distance logged with this gear.
)
