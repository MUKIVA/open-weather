package com.mukiva.openweather.glue.dashboard.mapper

import com.mukiva.core.data.entity.ConditionRemote
import com.mukiva.feature.dashboard.domain.model.Condition

object ConditionMapper {

    fun mapToDomain(item: ConditionRemote): Condition = with(item) {
        return Condition(
            text = text ?: "Unknown",
            icon = icon ?: "Unknown",
            code = code ?: 0
        )
    }

}