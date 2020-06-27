package com.mcwilliams.theninjamethod.ui.settings

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcwilliams.theninjamethod.network.Result
import com.mcwilliams.theninjamethod.strava.SessionRepository
import com.mcwilliams.theninjamethod.strava.model.strava.athlete.ActivityStats
import com.mcwilliams.theninjamethod.strava.model.strava.athlete.StravaAthlete
import com.mcwilliams.theninjamethod.ui.settings.repo.SettingsRepo
import kotlinx.coroutines.launch

class SettingsViewModel @ViewModelInject constructor(
    private val settingsRepo: SettingsRepo,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _activityStats = MutableLiveData<ActivityStats>()
    var activityStats: LiveData<ActivityStats> = _activityStats

    private var _detailedAthlete = MutableLiveData<StravaAthlete>()
    var detailedAthlete: LiveData<StravaAthlete> = _detailedAthlete

    private var _errorMessage = MutableLiveData<String>()
    var errorMessage: LiveData<String> = _errorMessage

    private var _isLoggedIn = MutableLiveData<Boolean>()
    var isLoggedIn: LiveData<Boolean> = _isLoggedIn

    init {
        _isLoggedIn.postValue(sessionRepository.isLoggedIn())
    }

    fun loginAthlete(code: String) {
        viewModelScope.launch {
            try {
                when (val response = settingsRepo.authAthlete(code)) {
                    is Result.Success -> {
                        loadDetailedAthlete()
                    }
                    is Result.Error -> {
                        _errorMessage.postValue(response.exception.toString())
                    }
                }
            } catch (e: Exception) {
                _errorMessage.postValue(e.message)
            }
        }
    }

    fun loadDetailedAthlete() {
        viewModelScope.launch {
            try {
                when (val response = settingsRepo.fetchAthlete()) {
                    is Result.Success -> {
                        _detailedAthlete.postValue(response.data)
                        response.data?.id?.let { loadActivityStats(it) }
                    }
                    is Result.Error -> {
                        _errorMessage.postValue(response.exception.toString())
                    }
                }
            } catch (e: java.lang.Exception) {
                _errorMessage.postValue(e.message)
            }
        }
    }

    fun loadActivityStats(id: Long) {
        viewModelScope.launch {
            try {
                when (val response = settingsRepo.fetchAthleteStats(id)) {
                    is Result.Success -> {
                        _activityStats.postValue(response.data)
                    }
                    is Result.Error -> {
                        _errorMessage.postValue(response.exception.toString())
                    }
                }
            } catch (e: java.lang.Exception) {
                _errorMessage.postValue(e.message)
            }
        }

    }

    fun logOff() {
        sessionRepository.logOff()
        _isLoggedIn.postValue(false)
    }
}