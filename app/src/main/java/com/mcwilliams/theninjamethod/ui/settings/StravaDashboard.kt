package com.mcwilliams.theninjamethod.ui.settings

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.util.*

@ExperimentalFoundationApi
@Composable
fun StravaDashboard() {
    val currentDate = LocalDate.now()
    val currentMonth = currentDate.month
    val firstDayOfMonth = currentDate.with(TemporalAdjusters.firstDayOfMonth())
    val lastDayOfMonth = currentDate.with(TemporalAdjusters.lastDayOfMonth())

    Log.d("TAG", "StravaDashboard: ${firstDayOfMonth.dayOfWeek}")
    Log.d("TAG", "StravaDashboard: ${lastDayOfMonth.dayOfWeek}")

//    val daysOfTheWeek = listOf("Mon", "Tues", "Wed", "Thurs", "Fri", "Sat", "Sun")

    Row {

    }
}

