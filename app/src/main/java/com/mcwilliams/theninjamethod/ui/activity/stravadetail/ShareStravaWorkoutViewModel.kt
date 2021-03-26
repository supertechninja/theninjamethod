package com.mcwilliams.theninjamethod.ui.activity.stravadetail

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mcwilliams.theninjamethod.strava.model.activitydetail.StravaActivityDetail

class ShareStravaWorkoutViewModel : ViewModel() {

    var _showSplits: MutableLiveData<Boolean> = MutableLiveData(true)
    var showSplits: LiveData<Boolean> = _showSplits

    var _cardColor: MutableLiveData<Color> = MutableLiveData(Color.White)
    var cardColor: LiveData<Color> = _cardColor

    fun toggleSplits() {
        _showSplits.postValue(!showSplits.value!!)
    }

    fun setColor(color: Color) {
        _cardColor.postValue(color)
    }
}