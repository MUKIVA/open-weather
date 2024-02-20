package com.mukiva.feature.dashboard.presentation

import androidx.lifecycle.viewModelScope
import com.mukiva.core.presentation.MultiStateViewModel
import com.mukiva.feature.dashboard.navigation.IDashboardRouter
import com.mukiva.feature.dashboard.domain.model.ILocation
import com.mukiva.feature.dashboard.domain.model.UnitsType
import com.mukiva.feature.dashboard.domain.repository.IForecastUpdater
import com.mukiva.feature.dashboard.domain.repository.ISettingsRepository
import com.mukiva.feature.dashboard.domain.usecase.GetAllLocationsUseCase
import com.mukiva.feature.dashboard.domain.usecase.GetCurrentWeatherUseCase
import com.mukiva.usecase.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getAllLocationsUseCase: GetAllLocationsUseCase,
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val router: IDashboardRouter,
    private val settingsRepository: ISettingsRepository,
    forecastUpdater: IForecastUpdater
) : MultiStateViewModel<IDashboardState>() {

    val position get() = mPosition

    private var mPosition = 0

    private val mMinorList
        get() = getState(IDashboardState.MinorState::class).list.toList()

    init {

        initState(
            IDashboardState.ScreenType::class,
            IDashboardState.ScreenType.default()
        )

        initState(
            IDashboardState.MainCardState::class,
            IDashboardState.MainCardState.default()
        )

        initState(
            IDashboardState.MinorState::class,
            IDashboardState.MinorState(emptyList())
        )
        viewModelScope.launch {
            settingsRepository.getUnitsTypeFlow()
                .collect { onUnitsTypeUpdate(it) }
        }

        forecastUpdater.observeUpdate { load() }

    }

    fun load() {
        viewModelScope.launch {
            modifyState(IDashboardState.ScreenType::class) {
                copy(type = IDashboardState.ScreenType.Type.LOADING)
            }
            val locations = getAllLocations()
            val stateWrappedLocations = wrapAsState(locations)
            modifyState(IDashboardState.ScreenType::class) {
                copy(
                    type = IDashboardState.ScreenType.Type.CONTENT
                )
            }
            modifyState(IDashboardState.MinorState::class) { copy(list = stateWrappedLocations) }
        }
    }

    fun goSelectLocations() {
        router.goLocationManager()
    }
    fun goSettings() {
        router.goSettings()
    }
    fun goForecast(pos: Int) {
        val item = mMinorList[mPosition]
        router.goFullForecast(item.location.name, pos)
    }
    fun onPageSelect(position: Int) {
        val minorList = mMinorList
        if (minorList.isEmpty()) return
        mPosition = position.coerceIn(0, minorList.lastIndex)

        viewModelScope.launch {
            if (minorList[mPosition].currentWeather == null)
                loadRemoteForecast(minorList[mPosition].location.name, mPosition)
            else
                loadCachedForecast(mPosition)
        }
    }


    private suspend fun loadRemoteForecast(locationName: String, position: Int) {
        modifyState(IDashboardState.MainCardState::class) {
            copy(type = IDashboardState.MainCardState.Type.LOADING)
        }
        when (val result = getCurrentWeatherUseCase(locationName)) {
            is ApiResult.Error -> modifyState(IDashboardState.ScreenType::class) {
                copy(type = IDashboardState.ScreenType.Type.ERROR)
            }
            is ApiResult.Success -> {
                modifyState(IDashboardState.MainCardState::class) {
                    copy(
                        type = IDashboardState.MainCardState.Type.CONTENT,
                        tempC = result.data.currentWeather?.tempC?.toInt() ?: 0,
                        tempF = result.data.currentWeather?.tempF?.toInt() ?: 0,
                        cityName = result.data.location.name,
                        iconUrl = result.data.currentWeather?.condition?.icon ?: ""
                    )
                }
                modifyState(IDashboardState.MinorState::class) {
                    copy(
                        list = list.updateItemAtPositionAsync(position) {
                            copy(
                                type = MinorWeatherState.Type.CONTENT,
                                currentWeather = result.data.currentWeather,
                                minimalForecastState = result.data.forecastState
                            )
                        }
                    )
                }
            }
        }
    }

    private fun loadCachedForecast(position: Int) {
        val minorList = mMinorList
        val minorWeatherState = minorList[position]

        modifyState(IDashboardState.MainCardState::class){
            copy(
                type = IDashboardState.MainCardState.Type.CONTENT,
                tempC = minorWeatherState.currentWeather?.tempC?.toInt() ?: 0,
                tempF = minorWeatherState.currentWeather?.tempF?.toInt() ?: 0,
                cityName = minorWeatherState.location.name,
                iconUrl = minorWeatherState.currentWeather?.condition?.icon ?: ""
            )
        }
    }

    private suspend fun getAllLocations(): List<ILocation> {
        return when (val result = getAllLocationsUseCase()) {
            is ApiResult.Error -> emptyList()
            is ApiResult.Success -> result.data.sortedBy { it.position }
        }
    }



    private suspend fun wrapAsState(locations: Collection<ILocation>): Collection<MinorWeatherState> {
        return locations.mapAsync {
            MinorWeatherState(
                type = MinorWeatherState.Type.LOADING,
                location = it,
                currentWeather = null,
                minimalForecastState = emptyList()
            )
        }
    }

    private suspend fun <T> Iterable<T>.updateItemAtPositionAsync(
        position: Int,
        transform: T.() -> T
    ): Collection<T> {
        val destination = ArrayList<T>(count())
        for ((index, item) in this.withIndex()) {
            if (index == position)
                destination.add(item.transform())
            else
                destination.add(item)
            delay(ASYNC_MAP_TICK)
        }
        return destination
    }

    private suspend fun <T, R> Iterable<T>.mapAsync(transform: (T) -> R): Collection<R> {
        val destination = ArrayList<R>(count())
        for (item in this) {
            destination.add(transform(item))
            delay(ASYNC_MAP_TICK)
        }
        return destination
    }

    private fun onUnitsTypeUpdate(unitsType: UnitsType) {
        modifyState(IDashboardState.ScreenType::class) {
            copy(unitsType = unitsType)
        }
    }

    companion object {
        private const val ASYNC_MAP_TICK = 250L
    }

}