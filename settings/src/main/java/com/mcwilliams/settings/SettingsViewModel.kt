package com.mcwilliams.settings

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcwilliams.appinf.SessionRepository
import com.mcwilliams.settings.model.AthleteStats
import com.mcwilliams.settings.model.StravaAthlete
import com.mcwilliams.settings.repo.SettingsRepo
import kotlinx.coroutines.launch

class SettingsViewModel @ViewModelInject constructor(
    private val settingsRepo: SettingsRepo,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private var _detailedAthlete = MutableLiveData<StravaAthlete>()
    var detailedAthlete: LiveData<StravaAthlete> = _detailedAthlete

    private var _athleteStats = MutableLiveData<AthleteStats>()
    var athleteStats: LiveData<AthleteStats> = _athleteStats

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
            _athleteStats.postValue(settingsRepo.fetchAthleteStats(detailedAthlete.id.toString()))
        }
    }

    fun logOff() {
        sessionRepository.logOff()
        _isLoggedIn.postValue(false)
    }
}