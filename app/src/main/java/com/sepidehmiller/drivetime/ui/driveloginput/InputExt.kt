package com.sepidehmiller.drivetime.ui.driveloginput

fun CharSequence.toSafeLong(): Long {
    return try {
        this.toString().toLong()
    } catch (e: NumberFormatException) {
        0L
    }
}
