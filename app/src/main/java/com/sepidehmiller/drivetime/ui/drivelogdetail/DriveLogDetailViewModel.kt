package com.sepidehmiller.drivetime.ui.drivelogdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sepidehmiller.drivetime.data.source.LocalRepository
import com.sepidehmiller.drivetime.ui.navigation.NavDestinationArgs
import com.sepidehmiller.drivetime.utils.AppDateFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class DriveLogDetailViewModel @Inject constructor(
    localRepository: LocalRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val id: Int = checkNotNull(savedStateHandle[NavDestinationArgs.DRIVE_LOG_ID_ARG])

    val uiState: StateFlow<DriveTimeDetailUiState> = localRepository.observeDriveTime(id)
        .map { localDriveTime ->
            DriveTimeDetailUiState.DriveTimeDetailUi(
                date = AppDateFormatter.formatMillis(localDriveTime.date, ZoneId.systemDefault()),
                dayHours = localDriveTime.dayHours.toString(),
                dayMinutes = localDriveTime.dayMinutes.toString(),
                nightHours = localDriveTime.nightHours.toString(),
                nightMinutes = localDriveTime.nightMinutes.toString(),
                comments = localDriveTime.comments
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DriveTimeDetailUiState.Loading
        )
}

sealed class DriveTimeDetailUiState {
    object Loading: DriveTimeDetailUiState()
    data class DriveTimeDetailUi(
        val date: String,
        val dayHours: String,
        val dayMinutes: String,
        val nightHours: String,
        val nightMinutes: String,
        val comments: String
    ): DriveTimeDetailUiState()
}

