package com.sepidehmiller.drivetime.data.source

import com.sepidehmiller.drivetime.data.source.local.DriveTimeDao
import com.sepidehmiller.drivetime.data.source.local.LocalDriveTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val localDataSource: DriveTimeDao
): LocalRepository {
    override suspend fun updateDriveTime(driveTime: LocalDriveTime) {
        withContext(Dispatchers.IO) {
            localDataSource.upsertDriveTime(driveTime)
        }
    }

    override fun observeDriveTimes(): Flow<List<LocalDriveTime>> {
        return localDataSource.observeDriveTimes()
    }

    override fun observeDriveTime(id: Int): Flow<LocalDriveTime> {
        return localDataSource.observeDriveTime(id)
    }
}
