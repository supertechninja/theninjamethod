package com.mcwilliams.theninjamethod.strava

enum class ApprovalPrompt(private val rawValue: String) {
    FORCE("force"), AUTO("auto");

    override fun toString(): String {
        return rawValue
    }

}