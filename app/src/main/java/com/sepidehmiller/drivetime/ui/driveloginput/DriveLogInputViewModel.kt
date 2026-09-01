package com.sepidehmiller.drivetime.ui.driveloginput

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sepidehmiller.drivetime.data.source.LocalRepository
import com.sepidehmiller.drivetime.data.source.local.LocalDriveTime
import com.sepidehmiller.drivetime.utils.AppDateFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class DriveLogInputViewModel @Inject constructor(
    val localRepository: LocalRepository
): ViewModel() {
    fun addDriveLog(
        dateString: String,
        dayHoursString: String,
        dayMinutesString: String,
        nightHoursString: String,
        nightMinutesString: String,
        comments: String
    ) {
        val dateMillis = AppDateFormatter.parseToMillis(dateString) ?: Instant.now().toEpochMilli()
        val dayHours = dayHoursString.toSafeLong()
        val nightHours = nightHoursString.toSafeLong()
        val dayMinutes = dayMinutesString.toSafeLong()
        val nightMinutes = nightMinutesString.toSafeLong()

        viewModelScope.launch {
            localRepository.updateDriveTime(
                LocalDriveTime(
                    date = dateMillis,
                    dayHours = dayHours,
                    dayMinutes = dayMinutes,
                    nightHours = nightHours,
                    nightMinutes = nightMinutes,
                    comments = comments
                )
            )
        }
    }
}
