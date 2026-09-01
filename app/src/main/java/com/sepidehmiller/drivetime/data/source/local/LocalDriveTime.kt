package com.sepidehmiller.drivetime.data.source.local

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(
    tableName = "drive_time"
)
data class LocalDriveTime(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long,
    val dayHours: Long,
    val dayMinutes: Long,
    val nightHours: Long,
    val nightMinutes: Long,
    val comments: String
)
