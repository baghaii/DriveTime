package com.sepidehmiller.drivetime.data.source.local

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface DriveTimeDao {
    @Query("SELECT * FROM drive_time ORDER BY date DESC")
    fun observeDriveTimes(): Flow<List<LocalDriveTime>>

    @Query("SELECT * FROM drive_time WHERE id = :id")
    fun observeDriveTime(id: Int): Flow<LocalDriveTime>

    @Upsert
    suspend fun upsertDriveTime(driveTime: LocalDriveTime)

    @Query("DELETE FROM drive_time WHERE id = :id")
    suspend fun deleteDriveTime(id: Int)
}
