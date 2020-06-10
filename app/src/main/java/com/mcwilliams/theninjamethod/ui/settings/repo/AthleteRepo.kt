package com.mcwilliams.theninjamethod.ui.settings.repo

import com.mcwilliams.theninjamethod.strava.model.DetailedAthlete
import com.mcwilliams.theninjamethod.network.Result

interface AthleteRepo {
//    fun saveAthlete(athlete: DetailedAthlete)

    //    fun getAthlete(): DetailedAthlete
    suspend fun fetchAthlete(): Result<DetailedAthlete?>
}