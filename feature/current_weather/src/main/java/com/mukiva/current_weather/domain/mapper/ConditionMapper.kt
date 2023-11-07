package com.mukiva.current_weather.domain.mapper

import com.mukiva.current_weather.data.ConditionJson
import com.mukiva.current_weather.domain.model.Condition
import javax.inject.Inject


class ConditionMapper @Inject constructor() {

    fun ConditionJson.mapToDomain(): Condition {
        return Condition(
            text = text ?: "Unknown",
            icon = icon ?: "Unknown",
            code = code ?: 0
        )
    }

}