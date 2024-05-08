package com.mukiva.feature.forecast.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import com.github.mukiva.weather_data.utils.RequestResult
import com.mukiva.feature.forecast.domain.UnitsType
import com.mukiva.feature.forecast.domain.repository.ISettingsRepository
import com.mukiva.feature.forecast.domain.usecase.GetFullForecastUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val settings: ISettingsRepository,
    private val getFullForecastUseCase: GetFullForecastUseCase,
) : ViewModel() {

    val state: StateFlow<ForecastState>
        get() = mState.asStateFlow()

    private val mState =
        MutableStateFlow<ForecastState>(ForecastState.Init)

    private val mUnitsTypeFlow: Flow<UnitsType>
        get() = settings.getUnitsTypeFlow()

    fun loadForecast(locationName: String) {
        val useCaseFlow = getFullForecastUseCase(locationName)
            .onEach { rr ->
                mState.update { asState(UnitsType.METRIC, rr) }
            }
//        val unitsTypeFlow = mUnitsTypeFlow
//            .onEach { Log.d("FLOW[S]", "R") }

//        unitsTypeFlow.combine(useCaseFlow) { unitsType, requestResult ->
//            mState.emit(asState(unitsType, requestResult))
//        }
    }

    fun dayState(position: Int): Flow<Pair<UnitsType, HourlyForecast>> {
        val unitsTypeFlow = mUnitsTypeFlow
        val hourlyForecastFlow = mState
            .filterIsInstance<ForecastState.Content>()
            .map { state -> state.hourlyForecast[position] }
        return unitsTypeFlow.combine(hourlyForecastFlow) { unitsType, hourlyForecast ->
            Pair(unitsType, hourlyForecast)
        }
    }

    private fun asState(
        unitsType: UnitsType,
        requestResult: RequestResult<List<HourlyForecast>>
    ): ForecastState {
        Log.d("A", "$requestResult")
        return when(requestResult) {
            is RequestResult.Error -> ForecastState.Error
            is RequestResult.InProgress -> ForecastState.Loading
            is RequestResult.Success -> ForecastState.Content(
                unitsType = unitsType,
                hourlyForecast = checkNotNull(requestResult.data),
            )
        }
    }

}