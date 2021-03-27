package com.mcwilliams.theninjamethod.ui.activity.stravadetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mcwilliams.theninjamethod.strava.model.activitydetail.StravaActivityDetail
import com.mcwilliams.theninjamethod.ui.ext.toLiveData
import io.reactivex.disposables.CompositeDisposable

class StravaDetailViewModel @ViewModelInject constructor(
    private val stravaDetailRepository: StravaWorkoutDetailRepository
) : ViewModel() {

    val rootDisposable = CompositeDisposable()
    var _detailedActivity: MutableLiveData<StravaActivityDetail> = MutableLiveData()
    var detailedActivity: LiveData<StravaActivityDetail> = _detailedActivity

    var _isLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    var isLoading: LiveData<Boolean> = _isLoading

    fun getDetailedActivities(id: Number) {
        detailedActivity =
            stravaDetailRepository.getStravaItemDetail(id).toLiveData(rootDisposable) { it }

        _isLoading.postValue(false)
    }
}