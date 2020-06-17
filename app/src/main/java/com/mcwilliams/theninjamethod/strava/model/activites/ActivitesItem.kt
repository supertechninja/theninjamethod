package com.mcwilliams.theninjamethod.strava.model.activites

data class ActivitesItem(
    val achievement_count: Int,
    val athlete: Athlete,
    val athlete_count: Int,
    val average_cadence: Double,
    val average_heartrate: Double,
    val average_speed: Double,
    val average_watts: Int,
    val comment_count: Int,
    val commute: Boolean,
    val device_watts: Boolean,
    val distance: Float,
    val elapsed_time: Int,
    val end_latlng: Any,
    val external_id: String,
    val flagged: Boolean,
    val from_accepted_tag: Boolean,
    val gear_id: String,
    val has_heartrate: Boolean,
    val has_kudoed: Boolean,
    val id: Number,
    val kilojoules: Int,
    val kudos_count: Int,
    val location_city: Any,
    val location_country: String,
    val location_state: Any,
    val manual: Boolean,
    val map: Map,
    val max_heartrate: Int,
    val max_speed: Double,
    val max_watts: Int,
    val moving_time: Int,
    val name: String,
    val photo_count: Int,
    val pr_count: Int,
    val `private`: Boolean,
    val resource_state: Int,
    val start_date: String,
    val start_date_local: String,
    val start_latlng: Any,
    val suffer_score: Int,
    val timezone: String,
    val total_elevation_gain: Float,
    val total_photo_count: Int,
    val trainer: Boolean,
    val type: String,
    val upload_id: Number,
    val utc_offset: Int,
    val weighted_average_watts: Int,
    val workout_type: Any
)