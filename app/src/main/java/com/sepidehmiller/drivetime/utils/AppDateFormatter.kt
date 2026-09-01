package com.sepidehmiller.drivetime.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object AppDateFormatter {
    private const val DATE_PATTERN = "MM/dd/yyyy"
    private val dateFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN)

    fun format(date: LocalDate): String {
        return date.format(dateFormatter)
    }

    /**
     * Formats epoch milliseconds to a date string.
     * Uses the provided time zone (defaults to system default).
     */
    fun formatMillis(millis: Long, zoneId: ZoneId = ZoneId.systemDefault()): String {
        return Instant.ofEpochMilli(millis)
            .atZone(zoneId)
            .toLocalDate()
            .format(dateFormatter)
    }

    /**
     * Parses a date string into a LocalDate.
     */
    fun parse(dateString: String): LocalDate? {
        return try {
            LocalDate.parse(dateString, dateFormatter)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Parses a date string into epoch milliseconds at the start of the day in the system default time zone.
     */
    fun parseToMillis(dateString: String): Long? {
        return parse(dateString)
            ?.atStartOfDay(ZoneId.systemDefault())
            ?.toInstant()
            ?.toEpochMilli()
    }
}
