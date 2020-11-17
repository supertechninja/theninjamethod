package com.mcwilliams.theninjamethod.ui.settings

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcwilliams.appinf.SessionRepository
import com.mcwilliams.data.ManualWorkoutsRepository
import com.mcwilliams.settings.model.AthleteStats
import com.mcwilliams.settings.model.StravaAthlete
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class SettingsViewModel @ViewModelInject constructor(
    private val settingsRepo: SettingsRepo,
    private val sessionRepository: SessionRepository,
    private val workoutsRepository: ManualWorkoutsRepository
) : ViewModel() {

    private var _detailedAthlete = MutableLiveData<StravaAthlete>()
    var detailedAthlete: LiveData<StravaAthlete> = _detailedAthlete

    var rootDisposable = CompositeDisposable()

    //    private var _workoutHistory = MutableLiveData<Int>()
    lateinit var workoutHistory: LiveData<Int>

    private var _athleteStats = MutableLiveData<AthleteStats>()
    var athleteStats: LiveData<AthleteStats> = _athleteStats

    private var _errorMessage = MutableLiveData<String>()
    var errorMessage: LiveData<String> = _errorMessage

    private var _isLoggedIn = MutableLiveData(false)
    var isLoggedIn: LiveData<Boolean> = _isLoggedIn

    init {
        _isLoggedIn.postValue(sessionRepository.isLoggedIn())
        viewModelScope.launch {
            workoutHistory = workoutsRepository.getWorkouts().toObservable().map { it.size }
                .toLiveData(rootDisposable) { it }
        }
    }

    fun loginAthlete(code: String) {
        viewModelScope.launch {
            settingsRepo.authAthlete(code)
            _isLoggedIn.postValue(sessionRepository.isLoggedIn())
            loadDetailedAthlete()
        }
    }

    fun loadDetailedAthlete() {
        viewModelScope.launch {
            val detailedAthlete = settingsRepo.fetchAthlete()
            _detailedAthlete.postValue(detailedAthlete!!)
            _athleteStats.postValue(settingsRepo.fetchAthleteStats(detailedAthlete.id.toString()))
        }
    }

    fun logOff() {
        sessionRepository.logOff()
        _isLoggedIn.postValue(false)
    }
}


//Transforms an observable into a livedata
fun <T, U> Observable<T>.toLiveData(
    disposable: CompositeDisposable,
    transform: (T) -> U
): LiveData<U> {
    return MutableLiveData<U>().also { liveData ->
        disposable.add(this.subscribeOn(Schedulers.io()).subscribe { nextValue ->
            liveData.postValue(transform(nextValue))
        })
    }
}