package com.mcwilliams.theninjamethod.ui.workouts

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcwilliams.theninjamethod.model.Exercise
import com.mcwilliams.theninjamethod.model.Workout
import com.mcwilliams.theninjamethod.model.WorkoutSet
import com.mcwilliams.theninjamethod.model.WorkoutType
import com.mcwilliams.theninjamethod.network.Result
import com.mcwilliams.theninjamethod.network.apis.WorkoutApi
import com.mcwilliams.theninjamethod.strava.SessionRepository
import com.mcwilliams.theninjamethod.ui.workouts.repo.ManualWorkoutsRepository
import com.mcwilliams.theninjamethod.ui.workouts.repo.WorkoutRepo
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class WorkoutListViewModel @ViewModelInject constructor(
    private val workoutApi: WorkoutApi,
    private val sessionRepo: SessionRepository,
    private val workoutRepo: WorkoutRepo,
    private val manualWorkoutsRepository: ManualWorkoutsRepository
) : ViewModel() {

    private val TAG = "WorkoutListViewModel"

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    var isRefreshing: Boolean = false
    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadWorkouts() }

    val workoutListAdapter: WorkoutListAdapter = WorkoutListAdapter()

    val workoutList: MutableList<Workout> = mutableListOf()

    init {
        loadWorkouts()
    }


    fun onStartWorkoutClick() {
        manualWorkoutsRepository.addWorkout(createDummyWorkout())
    }

    //Faking to populate db
    fun createDummyWorkout(): com.mcwilliams.theninjamethod.ui.workouts.db.Workout {
        val exercises = mutableListOf(
            Exercise(
                "Pullups", "Bodyweight", "Back",
                mutableListOf(WorkoutSet(185, 12), WorkoutSet(185, 12))
            ),
            Exercise(
                "Squat", "Bodyweight", "Back",
                mutableListOf(WorkoutSet(305, 12), WorkoutSet(345, 10))
            )
        )
        return com.mcwilliams.theninjamethod.ui.workouts.db.Workout(
            0,
            "Leg Day",
            LocalDate.now().toString(),
            exercises
        )
    }

    @SuppressLint("NewApi", "SimpleDateFormat")
    private fun loadWorkouts() {
        viewModelScope.launch {
//            val data = workoutApi.getWorkouts()
//            data.workouts.forEach {
//                it.workoutType = WorkoutType.LIFTING
//            }
//            workoutList.addAll(data.workouts)
//            isRefreshing = false
//            updateListView()
        }

        viewModelScope.launch {
            val manualWorkouts = manualWorkoutsRepository.getWorkouts()
            if (manualWorkouts!!.isNotEmpty()) {
                manualWorkouts.forEach {
                    workoutList.add(
                        Workout(
                            it.workoutDate,
                            "",
                            it.workoutName,
                            WorkoutType.STRAVA,
                            "",
                            ""
                        )
                    )
                }
            }
            Log.d(TAG, "loadWorkouts: $workoutList")
//            updateListView(workoutList)
        }

        if (sessionRepo.isLoggedIn()) {
            viewModelScope.launch {
                try {
                    when (val listOfActivitiesResponse = workoutRepo.getStravaActivities()) {
                        is Result.Success -> {
                            val listOfActivities = listOfActivitiesResponse.data
                            listOfActivities.forEach {
                                val dtf = DateTimeFormatter.ISO_DATE_TIME
                                val zdt: ZonedDateTime =
                                    ZonedDateTime.parse(it.start_date_local, dtf)
                                val localDateTime = zdt.toLocalDateTime()
                                val date = localDateTime.toLocalDate()
                                val time = localDateTime.toLocalTime()

                                val movingTime = "${it.moving_time / 60}m ${it.moving_time % 60}s"
                                val miles = getMiles(it.distance).round(2)
                                val milesString = "$miles mi"

                                val milesPerHour = (it.average_speed * 2.237).round(2)

                                val workoutItem = Workout(
                                    date.toString(),
                                    "${time.hour}:${time.minute}",
                                    it.name,
                                    WorkoutType.STRAVA,
                                    milesString,
                                    "$movingTime Pace: $milesPerHour mph"
                                )

                                workoutList.add(workoutItem)
                            }

                            //Group strava workouts by date
                            val dateKeyedWorkouts: MutableMap<String, MutableList<Workout>> =
                                mutableMapOf()
                            val listOfDates = workoutList.distinctBy { it.date }
                            for (date in listOfDates) {
                                val workoutsByDate: MutableList<Workout> = mutableListOf()
                                workoutList.forEach {
                                    if (it.date == date.date) {
                                        workoutsByDate.add(it)
                                    }
                                }
                                dateKeyedWorkouts[date.date] = workoutsByDate
                            }
                            updateListView(dateKeyedWorkouts.toList())
                        }
                        is Result.Error -> {
//                            _errorMessage.postValue(response.exception.toString())
                        }
                    }
                } catch (e: java.lang.Exception) {
                    if (!e.message.isNullOrEmpty()) {
                        Log.e(TAG, e.message)
                    }
                }
            }
        }
    }

    fun onWorkoutClicked(workout: Workout) {

    }

    fun getMiles(meters: Float): Double {
        return meters * 0.000621371192;
    }

    fun dropWorkoutDb(){
        viewModelScope.launch {
            manualWorkoutsRepository.nukeTable()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun LocalDate.format(date: LocalDate): String {
        return "${date.monthValue}/${date.dayOfMonth}/${date.year}"
    }

    fun Double.round(decimals: Int = 2): Double = "%.${decimals}f".format(this).toDouble()

    fun refreshData() {
        isRefreshing = true
        loadWorkouts()
    }

    private fun onRetrievePostListStart() {
        loadingVisibility.value = View.VISIBLE
        errorMessage.value = null
        isRefreshing = false
    }

    private fun onRetrievePostListFinish() {
        loadingVisibility.value = View.GONE
        isRefreshing = false
    }

    private fun updateListView(dateKeyedWorkouts: List<Pair<String, MutableList<Workout>>>) {
        loadingVisibility.value = View.GONE
        workoutListAdapter.updateWorkoutList(dateKeyedWorkouts)
    }

    private fun onRetrievePostListError() {
//        errorMessage.value = R.string.exercise_error
    }
}