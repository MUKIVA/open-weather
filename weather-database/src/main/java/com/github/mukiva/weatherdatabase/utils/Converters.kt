package com.github.mukiva.weatherdatabase.utils

import androidx.room.TypeConverter
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

internal class Converters {
    @TypeConverter
    fun epochToLocalDateTime(value: Int?): LocalDateTime? {
        return value?.let { seconds ->
            Instant.fromEpochSeconds(seconds.toLong())
                .toLocalDateTime(TimeZone.currentSystemDefault())
        }
    }

    @TypeConverter
    fun localDateTimeToEpochSeconds(value: LocalDateTime?): Int? {
        return value?.toInstant(TimeZone.UTC)?.epochSeconds?.toInt()
    }
}
