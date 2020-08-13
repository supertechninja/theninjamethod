package com.mcwilliams.settings

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcwilliams.appinf.SessionRepository
import com.mcwilliams.settings.model.ActivityTotal
import com.mcwilliams.settings.model.StravaAthlete
import com.mcwilliams.settings.repo.SettingsRepo
import kotlinx.coroutines.launch

class SettingsViewModel @ViewModelInject constructor(
    private val settingsRepo: SettingsRepo,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private var _detailedAthlete = MutableLiveData<StravaAthlete>()
    var detailedAthlete: LiveData<StravaAthlete> = _detailedAthlete

    private var _athleteStats =
        MutableLiveData<MutableList<Pair<String, MutableList<ActivityTotal?>>>>()
    var athleteStats: LiveData<MutableList<Pair<String, MutableList<ActivityTotal?>>>> =
        _athleteStats

    private var _errorMessage = MutableLiveData<String>()
    var errorMessage: LiveData<String> = _errorMessage

    private var _isLoggedIn = MutableLiveData(false)
    var isLoggedIn: LiveData<Boolean> = _isLoggedIn

    init {
        _isLoggedIn.postValue(sessionRepository.isLoggedIn())
    }

    fun loginAthlete(code: String) {
        viewModelScope.launch {
            settingsRepo.authAthlete(code)
            _isLoggedIn.postValue(sessionRepository.isLoggedIn())
            loadDetailedAthlete()
        }
    }

    fun loadDetailedAthlete() {
        viewModelScope.launch {
            val detailedAthlete = settingsRepo.fetchAthlete()
            _detailedAthlete.postValue(detailedAthlete!!)


            val athleteStats = settingsRepo.fetchAthleteStats(detailedAthlete.id.toString())

            val statsByType: MutableList<Pair<String, MutableList<ActivityTotal?>>> =
                mutableListOf()

            val rideStats = mutableListOf(
                athleteStats?.recent_ride_totals,
                athleteStats?.ytd_ride_totals,
                athleteStats?.all_ride_totals
            )

            val runStats = mutableListOf(
                athleteStats?.recent_run_totals,
                athleteStats?.ytd_run_totals,
                athleteStats?.all_run_totals
            )

            val swimStats = mutableListOf(
                athleteStats?.recent_swim_totals,
                athleteStats?.ytd_swim_totals,
                athleteStats?.all_swim_totals
            )

            statsByType.add(Pair("Ride", rideStats))
            statsByType.add(Pair("Run", runStats))
            statsByType.add(Pair("Swim", swimStats))

            _athleteStats.postValue(statsByType)
        }
    }

    fun logOff() {
        sessionRepository.logOff()
        _isLoggedIn.postValue(false)
    }
}