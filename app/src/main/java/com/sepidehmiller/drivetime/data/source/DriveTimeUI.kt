package com.sepidehmiller.drivetime.data.source

data class DriveTimeUi(
    val id: Int,
    val date: String,
    val dayHours: String,
    val dayMinutes: String,
    val nightHours: String,
    val nightMinutes: String,
    val comments: String
)
