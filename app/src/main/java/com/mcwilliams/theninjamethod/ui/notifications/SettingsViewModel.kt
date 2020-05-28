package com.mcwilliams.theninjamethod.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mcwilliams.theninjamethod.strava.authorize.TokenApi
import com.mcwilliams.theninjamethod.utils.viewmodel.BaseViewModel
import javax.inject.Inject

class SettingsViewModel : BaseViewModel() {

    @Inject
    lateinit var stravaApi: TokenApi


}