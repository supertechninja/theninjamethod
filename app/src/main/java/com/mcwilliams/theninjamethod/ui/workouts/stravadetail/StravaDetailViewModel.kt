package com.mcwilliams.theninjamethod.ui.workouts.stravadetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mcwilliams.theninjamethod.strava.model.activites.ActivitesItem

class StravaDetailViewModel @ViewModelInject constructor(
    private val workoutsRepository: StravaWorkoutRepository
) : ViewModel() {

    var detailedActivity: LiveData<ActivitesItem> = MutableLiveData()

    fun getDetailedActivities(id : Number) {
        detailedActivity = workoutsRepository.getStravaItemDetail(id)
    }
}