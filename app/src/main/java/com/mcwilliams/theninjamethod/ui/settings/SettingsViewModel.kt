package com.mcwilliams.theninjamethod.ui.settings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcwilliams.theninjamethod.network.Result
import com.mcwilliams.theninjamethod.strava.model.athlete.StravaAthlete
import com.mcwilliams.theninjamethod.ui.settings.data.Athlete
import com.mcwilliams.theninjamethod.ui.settings.repo.AthleteRepo
import com.mcwilliams.theninjamethod.ui.settings.repo.SettingsRepo
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val settingsRepo: SettingsRepo,
    private val athleteRepo: AthleteRepo
) : ViewModel() {

    private var _resultLogin = MutableLiveData<Athlete>()
    var resultLogin: LiveData<Athlete> = _resultLogin

    private var _detailedAthlete = MutableLiveData<StravaAthlete>()
    var detailedAthlete: LiveData<StravaAthlete> = _detailedAthlete

    private var _errorMessage = MutableLiveData<String>()
    var errorMessage: LiveData<String> = _errorMessage

    fun loginAthlete(code: String) {
        viewModelScope.launch {
            try {
                when (val response = settingsRepo.authAthlete(code)) {
                    is Result.Success -> {
                        _resultLogin.postValue(response.data)
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
                when (val response = athleteRepo.fetchAthlete()) {
                    is Result.Success -> {
                        Log.i("CHRIS", "result is success")
                        _detailedAthlete.postValue(response.data)
                    }
                    is Result.Error -> {
                        Log.i("CHRIS", "result is error")
                        _errorMessage.postValue(response.exception.toString())
                    }
                }
            } catch (e: java.lang.Exception) {
                _errorMessage.postValue(e.message)
            }
        }
    }
}