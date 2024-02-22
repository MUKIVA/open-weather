package com.mukiva.feature.dashboard.presentation

import com.mukiva.feature.dashboard.domain.model.UnitsType

sealed interface IDashboardState {

    data class ScreenType(
        val type: Type,
        val unitsType: UnitsType
    ) : IDashboardState {
        enum class Type {
            INIT,
            LOADING,
            CONTENT,
            EMPTY,
            ERROR
        }

        companion object {
            fun default() = ScreenType(
                type = Type.INIT,
                unitsType = UnitsType.METRIC
            )
        }
    }

    data class MainCardState(
        val type: Type,
        val tempC: Int,
        val tempF: Int,
        val cityName: String,
        val iconUrl: String
    ) : IDashboardState {
        enum class Type {
            LOADING,
            CONTENT
        }

        companion object {
            fun default() = MainCardState(
                type = Type.LOADING,
                tempC = 0,
                tempF = 0,
                cityName = "",
                iconUrl = ""
            )
        }
    }

    data class MinorState(
        val list: Collection<MinorWeatherState>
    ) : IDashboardState

}