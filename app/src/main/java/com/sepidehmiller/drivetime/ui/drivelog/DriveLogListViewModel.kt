package com.sepidehmiller.drivetime.ui.drivelog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sepidehmiller.drivetime.data.source.DriveTimeUi
import com.sepidehmiller.drivetime.data.source.LocalRepository
import com.sepidehmiller.drivetime.data.source.toDriveTimeUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DriveLogListViewModel @Inject constructor(localRepository: LocalRepository): ViewModel() {

    val driveTimes: StateFlow<DriveTimeState> =
        localRepository.observeDriveTimes().map { driveTimes ->
            if (driveTimes.isEmpty()) {
                DriveTimeState.Empty
            } else {
                val uiDriveTimes = driveTimes.map { it.toDriveTimeUi() }
                val (daySum, nightSum) = calculateHoursTotals(uiDriveTimes)
                DriveTimeState.Loaded(
                    driveTimes = uiDriveTimes,
                    daySum = daySum,
                    nightSum = nightSum
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DriveTimeState.Loading
        )
}

fun calculateHoursTotals(driveTimes: List<DriveTimeUi>): Pair<Double, Double> {
    var dayMinutesTotal = 0.0
    var nightMinutesTotal = 0.0

    for (item in driveTimes) {
        val dayH = item.dayHours.toDoubleOrNull() ?: 0.0
        val dayM = item.dayMinutes.toDoubleOrNull() ?: 0.0
        dayMinutesTotal += dayH * 60 + dayM

        val nightH = item.nightHours.toDoubleOrNull() ?: 0.0
        val nightM = item.nightMinutes.toDoubleOrNull() ?: 0.0
        nightMinutesTotal += nightH * 60 + nightM
    }
    return Pair(dayMinutesTotal / 60.0, nightMinutesTotal / 60.0)
}

sealed class DriveTimeState {
    object Loading: DriveTimeState()
    object Empty: DriveTimeState()
    data class Loaded(
        val driveTimes: List<DriveTimeUi>,
        val daySum: Double,
        val nightSum: Double
    ): DriveTimeState()
}
