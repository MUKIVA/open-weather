package com.github.mukiva.weatherdatabase.models

import androidx.room.ColumnInfo

public data class ConditionDbo(
    @ColumnInfo("text")
    var text: String,
    @ColumnInfo("icon")
    var icon: String,
    @ColumnInfo("code")
    var code: Int
)
