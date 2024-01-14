package com.mukiva.feature.dashboard_impl.domain.mapper

import com.mukiva.core.data.ConditionRemote
import com.mukiva.feature.dashboard_impl.domain.model.Condition
import javax.inject.Inject


class ConditionMapper @Inject constructor() {

    fun ConditionRemote.mapToDomain(): Condition {
        return Condition(
            text = text ?: "Unknown",
            icon = icon ?: "Unknown",
            code = code ?: 0
        )
    }

}