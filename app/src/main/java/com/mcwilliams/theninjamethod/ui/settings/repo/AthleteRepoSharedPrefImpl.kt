package com.mcwilliams.theninjamethod.ui.settings.repo

import android.util.Log
import com.mcwilliams.theninjamethod.network.Result
import com.mcwilliams.theninjamethod.strava.api.AthleteApi
import com.mcwilliams.theninjamethod.strava.model.athlete.StravaAthlete
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AthleteRepoSharedPrefImpl @Inject constructor(val api: AthleteApi) : AthleteRepo {

//    private var athlete: DetailedAthlete? = null

//    val preferences: SharedPreferences = context.getSharedPreferences(
//        context.getString(R.string.preference_file_key),
//        Context.MODE_PRIVATE
//    )

//    private lateinit var detailedAthlete: DetailedAthlete


//    override suspend fun authAthlete(code: String): Result<Athlete?> = withContext(Dispatchers.IO) {
//        val request = sessionRepo.getFirstTokens(code).athlete
//        Result.Success(request)
//    }

    override suspend fun fetchAthlete(): Result<StravaAthlete?> = withContext(Dispatchers.IO) {
        val request = api.getAthlete()
        Log.i("CHRIS", "fetchAthlete has a result")
        Result.Success(request)
    }

//    override fun saveAthlete(athlete: DetailedAthlete) {
//        this.athlete = athlete
//        with(preferences.edit()) {
//            putString(FIRST_NAME, athlete.firstname)
//            putString(LAST_NAME, athlete.lastname)
//            commit()
//        }
//    }

//    override fun getAthlete(): DetailedAthlete {
//        if(athlete == null) {
//            fetchAthlete()
//        }
//        var a: Athlete
//        if (athlete == null) {
//            with(preferences) {
//                athlete = DetailedAthlete()
//            }
//        }
//        return athlete!!
//    }

//    companion object {
//        private const val FIRST_NAME = "FIRST_NAME"
//        private const val LAST_NAME = "LAST_NAME"
//    }
}