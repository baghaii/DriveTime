package com.sepidehmiller.drivetime.data.source

import com.sepidehmiller.drivetime.data.source.local.LocalDriveTime
import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    suspend fun updateDriveTime(driveTime: LocalDriveTime)
    fun observeDriveTimes(): Flow<List<LocalDriveTime>>

    fun observeDriveTime(id: Int): Flow<LocalDriveTime>
}
