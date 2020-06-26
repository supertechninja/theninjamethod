package com.mcwilliams.theninjamethod.ui.workouts.stravadetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mcwilliams.theninjamethod.strava.model.activites.ActivitesItem

class StravaDetailViewModel @ViewModelInject constructor(
    private val workoutsRepository: StravaWorkoutRepository
) : ViewModel() {

    var _detailedActivity: MutableLiveData<ActivitesItem> = MutableLiveData()
    var detailedActivity: LiveData<ActivitesItem> = _detailedActivity

    fun getDetailedActivities(id: Number) {
        _detailedActivity.postValue(workoutsRepository.getStravaItemDetail(id))
    }
}