package com.mukiva.feature.forecast_impl.domain.usecase

import android.util.Log
import com.mukiva.core.network.IApiKeyProvider
import com.mukiva.core.network.IConnectionProvider
import com.mukiva.feature.forecast_impl.data.ForecastDayRemote
import com.mukiva.feature.forecast_impl.data.ForecastRemote
import com.mukiva.feature.forecast_impl.domain.Forecast
import com.mukiva.feature.forecast_impl.domain.IForecastGateway
import com.mukiva.usecase.ApiError
import com.mukiva.usecase.ApiResult
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class GetForecastUseCase @Inject constructor(
    private val connectionProvider: IConnectionProvider,
    private val forecastGateway: IForecastGateway,
    private val keyProvider: IApiKeyProvider
) {

    private val mTimeFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    private val mHourOfDayFormatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
    private val mCalendar = Calendar.getInstance()

    suspend operator fun invoke(q: String): ApiResult<List<Forecast>> {
        if (!connectionProvider.hasConnection()) {
            return ApiResult.Error(ApiError.NO_INTERNET)
        }

        return try {

            val data = forecastGateway.getCurrentWeather(
                key = keyProvider.apiKey,
                q = q,
                days = 3,
                aqi = "no",
                alerts = "no"
            )

            ApiResult.Success(data.forecast?.asDomain()
                ?: throw Exception("Failed to get a forecast")
            )
        } catch (e: Exception) {
            Log.d("GetForecastUseCase", "${e.message}")
            ApiResult.Error(ApiError.PARSE_ERROR)
        }
    }

    private fun ForecastRemote.asDomain(): List<Forecast> {
        return forecastDay.mapIndexed { index, item ->
            Forecast(
                id = index,
                dayAvgTempC = item.getDayAvgTempC(isMetricSystem = true),
                dayAvgTempF = item.getDayAvgTempC(isMetricSystem = false),
                nightAvgTempC = item.getNightAvgTempC(isMetricSystem = true),
                nightAvgTempF = item.getNightAvgTempC(isMetricSystem = false),
                date = item.date ?: throw Exception("PARSE FAIL")
            )
        }
    }

    private fun ForecastDayRemote.getDayAvgTempC(isMetricSystem: Boolean): Double {
        val sunrise = mHourOfDayFormatter.parse(astro?.sunrise ?: throw Exception("PARSE FAIL"))

        mCalendar.time = sunrise!!
        val sunriseHour = mCalendar.get(Calendar.HOUR_OF_DAY)

        val hourItem = hour.find {
            mCalendar.time = it.time?.let { time -> mTimeFormatter.parse(time) } ?: mCalendar.time
            mCalendar.get(Calendar.HOUR_OF_DAY) == sunriseHour
        }

        return when(isMetricSystem) {
            true -> hourItem?.tempC ?: 0.0
            false -> hourItem?.tempF ?: 0.0
        }
    }

    private fun ForecastDayRemote.getNightAvgTempC(isMetricSystem: Boolean): Double {
        val moonrise = mHourOfDayFormatter.parse(astro?.moonrise ?: throw Exception("PARSE FAIL"))

        mCalendar.time = moonrise!!
        val moonriseHour = mCalendar.get(Calendar.HOUR_OF_DAY)

        val hourItem = hour.find {
            mCalendar.time = it.time?.let { time -> mTimeFormatter.parse(time) } ?: mCalendar.time
            mCalendar.get(Calendar.HOUR_OF_DAY) == moonriseHour
        }

        return when(isMetricSystem) {
            true -> hourItem?.tempC ?: 0.0
            false -> hourItem?.tempF ?: 0.0
        }
    }




}