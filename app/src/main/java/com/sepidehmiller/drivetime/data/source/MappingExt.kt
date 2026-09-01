package com.sepidehmiller.drivetime.data.source

import com.sepidehmiller.drivetime.data.source.local.LocalDriveTime
import com.sepidehmiller.drivetime.utils.AppDateFormatter
import java.time.ZoneId


fun LocalDriveTime.toDriveTimeUi(): DriveTimeUi {
    return DriveTimeUi(
        id = id,
        date = AppDateFormatter.formatMillis(date, ZoneId.systemDefault()),
        dayHours = dayHours.toString(),
        dayMinutes = dayMinutes.toString(),
        nightHours = nightHours.toString(),
        nightMinutes = nightMinutes.toString(),
        comments = comments
    )
}
