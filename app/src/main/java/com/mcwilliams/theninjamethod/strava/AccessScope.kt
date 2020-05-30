package com.mcwilliams.theninjamethod.strava

enum class AccessScope(private val rawValue: String) {
    PUBLIC("public"), WRITE("write"), VIEW_PRIVATE("view_private"), VIEW_PRIVATE_WRITE("activity:write,read");

    override fun toString(): String {
        return rawValue
    }

}