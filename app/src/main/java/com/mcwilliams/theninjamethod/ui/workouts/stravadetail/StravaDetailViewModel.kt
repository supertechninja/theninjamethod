package com.mcwilliams.theninjamethod.ui.workouts.stravadetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mcwilliams.theninjamethod.strava.model.activites.ActivitesItem
import com.mcwilliams.theninjamethod.ui.workouts.repo.StravaWorkoutRepository

class StravaDetailViewModel @ViewModelInject constructor(
    private val workoutsRepository: StravaWorkoutRepository
) : ViewModel() {

    private var _detailedActivity = MutableLiveData<ActivitesItem>()
    var detailedActivity: LiveData<ActivitesItem> = _detailedActivity

    fun getDetailedActivities(id : Number) {
        val detailedActivity = workoutsRepository.getStravaItemDetail(id)
        _detailedActivity.postValue(detailedActivity)
    }
}