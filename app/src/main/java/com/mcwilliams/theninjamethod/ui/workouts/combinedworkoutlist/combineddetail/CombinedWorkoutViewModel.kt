package com.mcwilliams.theninjamethod.ui.workouts.combinedworkoutlist.combineddetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mcwilliams.theninjamethod.strava.model.activitydetail.StravaActivityDetail
import com.mcwilliams.theninjamethod.ui.ext.toLiveData
import com.mcwilliams.theninjamethod.ui.workouts.manualworkoutdetail.ManualWorkoutsRepository
import com.mcwilliams.theninjamethod.ui.workouts.manualworkoutdetail.db.Workout
import com.mcwilliams.theninjamethod.ui.workouts.stravadetail.StravaWorkoutDetailRepository
import io.reactivex.disposables.CompositeDisposable

class CombinedWorkoutViewModel @ViewModelInject constructor(
    private val stravaDetailRepository: StravaWorkoutDetailRepository,
    private val manualWorkoutsRepository: ManualWorkoutsRepository
) : ViewModel() {

    val rootDisposable = CompositeDisposable()
    var _detailedActivity: MutableLiveData<StravaActivityDetail> = MutableLiveData()
    var detailedActivity: LiveData<StravaActivityDetail> = _detailedActivity

    var _detailedActivities: MutableLiveData<List<StravaActivityDetail>> = MutableLiveData()
    var detailedActivities: LiveData<List<StravaActivityDetail>> = _detailedActivities

    var _workout: MutableLiveData<Workout> = MutableLiveData()
    var workout: LiveData<Workout> = _workout

    fun getManualWorkoutDetail(id: Number) {
        _workout.postValue(manualWorkoutsRepository.getWorkoutDetail(id))
    }

    //TODO what if multiple activities
    fun getDetailedActivities(id: Number) {
        detailedActivity =
            stravaDetailRepository.getStravaItemDetail(id).toLiveData(rootDisposable) { it }
    }

    fun getMultipleDetailedActivities(id: List<Number>) {
        detailedActivities =
            stravaDetailRepository.getMultipleStravaDetails(id).toLiveData(rootDisposable) { it }
    }

}