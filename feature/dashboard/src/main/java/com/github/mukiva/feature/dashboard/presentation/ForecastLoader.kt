package com.github.mukiva.feature.dashboard.presentation

import com.github.mukiva.feature.dashboard.domain.model.Forecast
import com.github.mukiva.feature.dashboard.domain.usecase.GetCurrentUseCase
import com.github.mukiva.weatherdata.utils.RequestResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class ForecastLoader @Inject constructor(
    private val getCurrentUseCase: GetCurrentUseCase
) : IForecastLoader {

    private val mLoaderScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val mLoadingFlows = HashMap<Long, StateFlow<ICurrentState>>()

    override fun loadForecast(locationId: Long) {
        mLoaderScope.launch {
            mLoadingFlows[locationId] = createState(locationId)
        }
    }

    override suspend fun provideForecastState(locationId: Long): Flow<ICurrentState> {
        return mLoadingFlows[locationId] ?: createState(locationId).apply {
            mLoadingFlows[locationId] = this
        }
    }

    override fun cleanLoadedData() {
        mLoaderScope.launch {
            for (key in mLoadingFlows.keys) {
                mLoadingFlows[key] = createState(key)
            }
        }
    }

    private suspend fun createState(locationId: Long): StateFlow<ICurrentState> {
        return getCurrentUseCase.invoke(locationId)
            .flowOn(Dispatchers.Default)
            .map(::asState)
            .stateIn(
                scope = mLoaderScope,
                started = SharingStarted.Lazily,
                initialValue = ICurrentState.Init
            )
    }

    private fun asState(
        requestResult: RequestResult<Forecast>
    ): ICurrentState = when (requestResult) {
        is RequestResult.Error -> ICurrentState.Error
        is RequestResult.InProgress -> ICurrentState.Loading
        is RequestResult.Success -> ICurrentState.Content(
            currentState = requestResult.data.current,
            precipitation = requestResult.data.precipitation,
            astro = requestResult.data.astro,
            forecastState = ForecastState(requestResult.data.dayForecasts),
        )
    }

}