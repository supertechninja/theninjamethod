package com.mcwilliams.theninjamethod.ui.settings

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcwilliams.appinf.SessionRepository
import com.mcwilliams.theninjamethod.strava.model.athlete.StravaAthlete
import com.mcwilliams.theninjamethod.ui.settings.repo.SettingsRepo
import kotlinx.coroutines.launch

class SettingsViewModel @ViewModelInject constructor(
    private val settingsRepo: SettingsRepo,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private var _detailedAthlete = MutableLiveData<StravaAthlete>()
    var detailedAthlete: LiveData<StravaAthlete> = _detailedAthlete

    private var _errorMessage = MutableLiveData<String>()
    var errorMessage: LiveData<String> = _errorMessage

    private var _isLoggedIn = MutableLiveData<Boolean>()
    var isLoggedIn : LiveData<Boolean> = _isLoggedIn

    init {
        _isLoggedIn.postValue(sessionRepository.isLoggedIn())
    }

    fun loginAthlete(code: String) {
        viewModelScope.launch {
            settingsRepo.authAthlete(code)
            loadDetailedAthlete()
        }
    }

    fun loadDetailedAthlete() {
        viewModelScope.launch {
            _detailedAthlete.postValue(settingsRepo.fetchAthlete())
        }
    }

    fun logOff (){
        sessionRepository.logOff()
        _isLoggedIn.postValue(false)
    }
}