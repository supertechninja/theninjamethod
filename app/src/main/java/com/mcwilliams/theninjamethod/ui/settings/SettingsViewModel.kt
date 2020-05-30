package com.mcwilliams.theninjamethod.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcwilliams.theninjamethod.network.Result
import com.mcwilliams.theninjamethod.ui.settings.data.Athlete
import com.mcwilliams.theninjamethod.ui.settings.repo.SettingsRepo
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val settingsRepo: SettingsRepo
) : ViewModel() {

    private var _resultLogin = MutableLiveData<Athlete>()
    var resultLogin: LiveData<Athlete> = _resultLogin

    private var _errorMessage = MutableLiveData<String>()
    var errorMessage: LiveData<String> = _errorMessage

    fun loginAthlete(code :String) {
//        _showLoading.postValue(true)
        viewModelScope.launch {
            try {
                when ( val response = settingsRepo.authAthlete(code)) {
                    is Result.Success -> {
//                        _showLoading.postValue(false)
                        _resultLogin.postValue(response.data)
                    }
                    is Result.Error -> {
//                        _showLoading.postValue(false)
                        _errorMessage.postValue(response.exception.toString())
                    }
                }
            } catch (e: Exception) {
//                _showLoading.postValue(false)
                _errorMessage.postValue(e.message)
            }
        }
    }
}