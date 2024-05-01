package com.github.mukiva.weather_database.models

import androidx.room.ColumnInfo

data class ConditionDbo(
    @ColumnInfo("text")
    var text: String,
    @ColumnInfo("icon")
    var icon: String,
    @ColumnInfo("code")
    var code: Int
)