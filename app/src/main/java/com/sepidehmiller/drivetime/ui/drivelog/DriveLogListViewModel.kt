package com.sepidehmiller.drivetime.ui.drivelog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sepidehmiller.drivetime.data.source.DriveTimeUi
import com.sepidehmiller.drivetime.data.source.LocalRepository
import com.sepidehmiller.drivetime.data.source.toDriveTimeUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@HiltViewModel
class DriveLogListViewModel @Inject constructor(localRepository: LocalRepository): ViewModel() {

    val driveTimes: SharedFlow<DriveTimeState> =
        localRepository.observeDriveTimes().map { driveTimes ->
            if (driveTimes.isEmpty()) {
                DriveTimeState.Empty
            } else {
                DriveTimeState.Loaded(driveTimes.map{it.toDriveTimeUi()})
            }
        }.shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                replay = 1
            )
}

private fun totalHours(driveHours: List<Double>, driveMinutes: List<Double>): Double {
    val hours = driveHours.sum()
    val minutes = driveMinutes.sum()
    return hours + minutes / 60.0
}

sealed class DriveTimeState {
    object Loading: DriveTimeState()
    object Empty: DriveTimeState()
    data class Loaded(
        val driveTimes: List<DriveTimeUi>,
        val daySum: Double = totalHours(
            driveHours = driveTimes.map{ it.dayHours.toDouble() },
            driveMinutes = driveTimes.map{ it.dayMinutes.toDouble()}),
        val nightSum: Double = totalHours(
            driveHours = driveTimes.map{ it.nightHours.toDouble() },
            driveMinutes = driveTimes.map{ it.nightMinutes.toDouble()}
        )
    ): DriveTimeState()
}
