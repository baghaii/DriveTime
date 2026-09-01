package com.sepidehmiller.drivetime.data.source.local

import androidx.room3.Database
import androidx.room3.RoomDatabase

@Database(entities = [LocalDriveTime::class], version = 1, exportSchema = false)
abstract class DriveTimeDatabase: RoomDatabase() {
    abstract fun driveTimeDao(): DriveTimeDao
}
