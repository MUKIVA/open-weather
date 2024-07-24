package com.github.mukiva.weatherdata.utils

import com.github.mukiva.weatherapi.models.AstroDto
import com.github.mukiva.weatherapi.models.ConditionDto
import com.github.mukiva.weatherapi.models.CurrentDto
import com.github.mukiva.weatherapi.models.DayDto
import com.github.mukiva.weatherapi.models.ForecastDayDto
import com.github.mukiva.weatherapi.models.ForecastDto
import com.github.mukiva.weatherapi.models.ForecastWithCurrentAndLocationDto
import com.github.mukiva.weatherapi.models.HourDto
import com.github.mukiva.weatherapi.models.LocationDto
import com.github.mukiva.weatherdata.models.AstroData
import com.github.mukiva.weatherdata.models.ConditionData
import com.github.mukiva.weatherdata.models.CurrentData
import com.github.mukiva.weatherdata.models.DayData
import com.github.mukiva.weatherdata.models.ForecastData
import com.github.mukiva.weatherdata.models.ForecastDayData
import com.github.mukiva.weatherdata.models.ForecastWithCurrentAndLocationData
import com.github.mukiva.weatherdata.models.HourData
import com.github.mukiva.weatherdata.models.LocationData
import com.github.mukiva.weatherdatabase.models.AstroDbo
import com.github.mukiva.weatherdatabase.models.ConditionDbo
import com.github.mukiva.weatherdatabase.models.CurrentDbo
import com.github.mukiva.weatherdatabase.models.DayDbo
import com.github.mukiva.weatherdatabase.models.ForecastDayDbo
import com.github.mukiva.weatherdatabase.models.HourDbo
import com.github.mukiva.weatherdatabase.models.LocationDbo
import com.github.mukiva.weatherdatabase.relations.ForecastDayWithHours
import com.github.mukiva.weatherdatabase.relations.ForecastWithDays
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import com.github.mukiva.weatherdatabase.relations.ForecastWithCurrentAndLocation as RelationForecastWithCurrentAndLocation

internal fun ForecastWithCurrentAndLocationDto.toDomain(): ForecastWithCurrentAndLocationData {
    return ForecastWithCurrentAndLocationData(
        locationData = location.toLocation(),
        currentData = current.toCurrent(),
        forecastData = forecast.toForecast(),
    )
}

internal fun RelationForecastWithCurrentAndLocation.toDomain(): ForecastWithCurrentAndLocationData {
    return ForecastWithCurrentAndLocationData(
        locationData = location.toLocation(),
        currentData = current.toCurrent(),
        forecastData = forecast.toForecast(),
    )
}

internal fun ForecastDto.toForecast(): ForecastData {
    return ForecastData(
        forecastDayData = forecastDay.map { it.toForecastDay() },
    )
}

internal fun ForecastWithDays.toForecast(): ForecastData {
    return ForecastData(
        forecastDayData = forecastDays.map { it.toForecastDay() },
    )
}

internal fun ForecastDayWithHours.toForecastDay(): ForecastDayData {
    return ForecastDayData(
        dateEpoch = forecastDay.dateEpoch,
        dayData = forecastDay.day.toDay(),
        astroData = forecastDay.astro.toAstro(),
        hourData = hours.map { it.toHour() },
    )
}

internal fun AstroDbo.toAstro(): AstroData {
    return AstroData(
        sunrise = sunrise,
        sunset = sunset,
        moonrise = moonrise,
        moonset = moonset,
        moonPhase = moonPhase,
        moonIllumination = moonIllumination,
        isMoonUp = isMoonUp,
        isSunUp = isSunUp,
    )
}

internal fun DayDbo.toDay(): DayData {
    return DayData(
        maxTempC = maxTempC,
        maxTempF = maxTempF,
        minTempC = minTempC,
        minTempF = minTempF,
        avgTempC = avgTempC,
        avgTempF = avgTempF,
        maxWindMph = maxWindMph,
        maxWindKph = maxWindKph,
        totalPrecipMm = totalPrecipMm,
        totalPrecipIn = totalPrecipIn,
        totalSnowCm = totalSnowCm,
        avgVisKm = avgVisKm,
        avgVisMiles = avgVisMiles,
        avgHumidity = avgHumidity,
        dailyWillItRain = dailyWillItRain,
        dailyChanceOfRain = dailyChanceOfRain,
        dailyWillItSnow = dailyWillItSnow,
        dailyChanceOfSnow = dailyChanceOfSnow,
        uv = uv,
        conditionData = condition.toCondition(),
    )
}

internal fun DayDto.toDay(): DayData {
    return DayData(
        maxTempC = maxTempC,
        maxTempF = maxTempF,
        minTempC = minTempC,
        minTempF = minTempF,
        avgTempC = avgTempC,
        avgTempF = avgTempF,
        maxWindMph = maxWindMph,
        maxWindKph = maxWindKph,
        totalPrecipMm = totalPrecipMm,
        totalPrecipIn = totalPrecipIn,
        totalSnowCm = totalSnowCm,
        avgVisKm = avgVisKm,
        avgVisMiles = avgVisMiles,
        avgHumidity = avgHumidity,
        dailyWillItRain = dailyWillItRain,
        dailyChanceOfRain = dailyChanceOfRain,
        dailyWillItSnow = dailyWillItSnow,
        dailyChanceOfSnow = dailyChanceOfSnow,
        uv = uv,
        conditionData = condition.toCondition(),
    )
}

internal fun AstroDto.toAstro(): AstroData {
    return AstroData(
        sunrise = sunrise,
        sunset = sunset,
        moonrise = moonrise,
        moonset = moonset,
        moonPhase = moonPhase,
        moonIllumination = moonIllumination,
        isMoonUp = isMoonUp,
        isSunUp = isSunUp,
    )
}

internal fun CurrentDto.toCurrent(): CurrentData {
    return CurrentData(
        lastUpdatedEpoch = lastUpdatedEpoch,
        tempC = tempC,
        tempF = tempF,
        isDay = isDay,
        conditionData = condition.toCondition(),
        windMph = windMph,
        windKph = windKph,
        windDegree = windDegree,
        windDir = windDir,
        pressureMb = pressureMb,
        pressureIn = pressureIn,
        precipMm = precipMm,
        precipIn = precipIn,
        humidity = humidity,
        cloud = cloud,
        feelslikeC = feelslikeC,
        feelslikeF = feelslikeF,
        visKm = visKm,
        visMiles = visMiles,
        uv = uv,
        gustMph = gustMph,
        gustKph = gustKph,
    )
}

internal fun CurrentDbo.toCurrent(): CurrentData {
    return CurrentData(
        lastUpdatedEpoch = lastUpdatedEpoch,
        tempC = tempC,
        tempF = tempF,
        isDay = isDay,
        conditionData = condition.toCondition(),
        windMph = windMph,
        windKph = windKph,
        windDegree = windDegree,
        windDir = windDir,
        pressureMb = pressureMb,
        pressureIn = pressureIn,
        precipMm = precipMm,
        precipIn = precipIn,
        humidity = humidity,
        cloud = cloud,
        feelslikeC = feelslikeC,
        feelslikeF = feelslikeF,
        visKm = visKm,
        visMiles = visMiles,
        uv = uv,
        gustMph = gustMph,
        gustKph = gustKph,
    )
}

internal fun HourDto.toDbo(
    forecastDayId: Long
): HourDbo {
    return HourDbo(
        id = 0,
        timeEpoch = timeEpoch,
        tempC = tempC,
        tempF = tempF,
        isDay = isDay,
        windMph = windMph,
        windKph = windKph,
        windDegree = windDegree,
        windDir = windDir,
        pressureMb = pressureMb,
        pressureIn = pressureIn,
        precipMm = precipMm,
        precipIn = precipIn,
        snowCm = snowCm,
        humidity = humidity,
        cloud = cloud,
        feelsLikeC = feelsLikeC,
        feelsLikeF = feelsLikeF,
        windChillC = windChillC,
        windChillF = windChillF,
        heatIndexC = heatIndexC,
        heatIndexF = heatIndexF,
        dewPointC = dewPointC,
        dewPointF = dewPointF,
        willItRain = willItRain,
        chanceOfRain = chanceOfRain,
        willItSnow = willItSnow,
        chanceOfSnow = chanceOfSnow,
        visKm = visKm,
        visMiles = visMiles,
        gustMph = gustMph,
        gustKph = gustKph,
        uv = uv,
        condition = condition.toDbo(),
        forecastDayId = forecastDayId,
    )
}

internal fun HourDto.toHour(): HourData {
    return HourData(
        timeEpoch = timeEpoch,
        tempC = tempC,
        tempF = tempF,
        isDay = isDay,
        windMph = windMph,
        windKph = windKph,
        windDegree = windDegree,
        windDir = windDir,
        pressureMb = pressureMb,
        pressureIn = pressureIn,
        precipMm = precipMm,
        precipIn = precipIn,
        snowCm = snowCm,
        humidity = humidity,
        cloud = cloud,
        feelsLikeC = feelsLikeC,
        feelsLikeF = feelsLikeF,
        windChillC = windChillC,
        windChillF = windChillF,
        heatIndexC = heatIndexC,
        heatIndexF = heatIndexF,
        dewPointC = dewPointC,
        dewPointF = dewPointF,
        willItRain = willItRain,
        chanceOfRain = chanceOfRain,
        willItSnow = willItSnow,
        chanceOfSnow = chanceOfSnow,
        visKm = visKm,
        visMiles = visMiles,
        gustMph = gustMph,
        gustKph = gustKph,
        uv = uv,
        conditionData = condition.toCondition()
    )
}

internal fun HourDbo.toHour(): HourData {
    return HourData(
        timeEpoch = timeEpoch,
        tempC = tempC,
        tempF = tempF,
        isDay = isDay,
        windMph = windMph,
        windKph = windKph,
        windDegree = windDegree,
        windDir = windDir,
        pressureMb = pressureMb,
        pressureIn = pressureIn,
        precipMm = precipMm,
        precipIn = precipIn,
        snowCm = snowCm,
        humidity = humidity,
        cloud = cloud,
        feelsLikeC = feelsLikeC,
        feelsLikeF = feelsLikeF,
        windChillC = windChillC,
        windChillF = windChillF,
        heatIndexC = heatIndexC,
        heatIndexF = heatIndexF,
        dewPointC = dewPointC,
        dewPointF = dewPointF,
        willItRain = willItRain,
        chanceOfRain = chanceOfRain,
        willItSnow = willItSnow,
        chanceOfSnow = chanceOfSnow,
        visKm = visKm,
        visMiles = visMiles,
        gustMph = gustMph,
        gustKph = gustKph,
        uv = uv,
        conditionData = condition.toCondition()
    )
}

internal fun ForecastDayDto.toDbo(
    forecastId: Long
): ForecastDayDbo {
    return ForecastDayDbo(
        id = 0,
        dateEpoch = dateEpoch,
        day = day.toDbo(),
        astro = astro.toDbo(),
        forecastId = forecastId
    )
}

internal fun ForecastDayDto.toForecastDay(): ForecastDayData {
    return ForecastDayData(
        dateEpoch = dateEpoch,
        dayData = day.toDay(),
        astroData = astro.toAstro(),
        hourData = hour.map { it.toHour() },
    )
}

internal fun AstroDto.toDbo(): AstroDbo {
    return AstroDbo(
        sunrise = sunrise,
        sunset = sunset,
        moonrise = moonrise,
        moonset = moonset,
        moonPhase = moonPhase,
        moonIllumination = moonIllumination,
        isMoonUp = isMoonUp,
        isSunUp = isSunUp
    )
}

internal fun DayDto.toDbo(): DayDbo {
    return DayDbo(
        maxTempC = maxTempC,
        maxTempF = maxTempF,
        minTempC = minTempC,
        minTempF = minTempF,
        avgTempC = avgTempC,
        avgTempF = avgTempF,
        maxWindMph = maxWindMph,
        maxWindKph = maxWindKph,
        totalPrecipMm = totalPrecipMm,
        totalPrecipIn = totalPrecipIn,
        totalSnowCm = totalSnowCm,
        avgVisKm = avgVisKm,
        avgVisMiles = avgVisMiles,
        avgHumidity = avgHumidity,
        dailyWillItRain = dailyWillItRain,
        dailyChanceOfRain = dailyChanceOfRain,
        dailyWillItSnow = dailyWillItSnow,
        dailyChanceOfSnow = dailyChanceOfSnow,
        uv = uv,
        condition = condition.toDbo()
    )
}

internal fun CurrentDto.toDbo(): CurrentDbo {
    return CurrentDbo(
        id = 0,
        lastUpdatedEpoch = lastUpdatedEpoch,
        tempC = tempC,
        tempF = tempF,
        isDay = isDay,
        condition = condition.toDbo(),
        windMph = windMph,
        windKph = windKph,
        windDegree = windDegree,
        windDir = windDir,
        pressureMb = pressureMb,
        pressureIn = pressureIn,
        precipMm = precipMm,
        precipIn = precipIn,
        humidity = humidity,
        cloud = cloud,
        feelslikeC = feelslikeC,
        feelslikeF = feelslikeF,
        visKm = visKm,
        visMiles = visMiles,
        uv = uv,
        gustMph = gustMph,
        gustKph = gustKph,
    )
}

internal fun ConditionDto.toDbo(): ConditionDbo {
    return ConditionDbo(
        text = text,
        icon = icon,
        code = code
    )
}

internal fun ConditionDto.toCondition(): ConditionData {
    return ConditionData(
        text = text,
        icon = icon,
        code = code,
    )
}

internal fun ConditionDbo.toCondition(): ConditionData {
    return ConditionData(
        text = text,
        icon = icon,
        code = code,
    )
}

internal fun LocationDto.toLocation(): LocationData {
    return LocationData(
        id = id,
        name = name,
        region = region,
        country = country,
        lat = lat,
        lon = lon,
        tzId = tzId ?: "",
        localtimeEpoch = localtimeEpoch ?: Clock.System.now().toLocalDateTime(TimeZone.UTC),
        priority = 0,
    )
}

internal fun LocationData.toDbo(): LocationDbo {
    return LocationDbo(
        id = id,
        name = name,
        region = region,
        country = country,
        lat = lat,
        lon = lon,
        tzId = tzId,
        localtimeEpoch = localtimeEpoch,
        priority = priority,
    )
}

internal fun LocationDbo.toLocation(): LocationData {
    return LocationData(
        id = id,
        name = name,
        region = region,
        country = country,
        lat = lat,
        lon = lon,
        tzId = tzId,
        localtimeEpoch = localtimeEpoch,
        priority = priority,
    )
}
