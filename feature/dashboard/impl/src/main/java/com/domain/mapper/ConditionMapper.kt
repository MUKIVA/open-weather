package com.domain.mapper

import com.domain.model.Condition
import javax.inject.Inject


class ConditionMapper @Inject constructor() {

    fun com.data.ConditionJson.mapToDomain(): Condition {
        return Condition(
            text = text ?: "Unknown",
            icon = icon ?: "Unknown",
            code = code ?: 0
        )
    }

}