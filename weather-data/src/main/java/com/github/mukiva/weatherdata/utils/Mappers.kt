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
import com.github.mukiva.weatherdata.models.Astro
import com.github.mukiva.weatherdata.models.Condition
import com.github.mukiva.weatherdata.models.Current
import com.github.mukiva.weatherdata.models.Day
import com.github.mukiva.weatherdata.models.Forecast
import com.github.mukiva.weatherdata.models.ForecastDay
import com.github.mukiva.weatherdata.models.ForecastWithCurrentAndLocation
import com.github.mukiva.weatherdata.models.Hour
import com.github.mukiva.weatherdata.models.Location
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

internal fun ForecastWithCurrentAndLocationDto.toDomain(): ForecastWithCurrentAndLocation {
    return ForecastWithCurrentAndLocation(
        location = location.toLocation(),
        current = current.toCurrent(),
        forecast = forecast.toForecast(),
    )
}

internal fun RelationForecastWithCurrentAndLocation.toDomain(): ForecastWithCurrentAndLocation {
    return ForecastWithCurrentAndLocation(
        location = location.toLocation(),
        current = current.toCurrent(),
        forecast = forecast.toForecast(),
    )
}

internal fun ForecastDto.toForecast(): Forecast {
    return Forecast(
        forecastDay = forecastDay.map { it.toForecastDay() },
    )
}

internal fun ForecastWithDays.toForecast(): Forecast {
    return Forecast(
        forecastDay = forecastDays.map { it.toForecastDay() },
    )
}

internal fun ForecastDayWithHours.toForecastDay(): ForecastDay {
    return ForecastDay(
        dateEpoch = forecastDay.dateEpoch,
        day = forecastDay.day.toDay(),
        astro = forecastDay.astro.toAstro(),
        hour = hours.map { it.toHour() },
    )
}

internal fun AstroDbo.toAstro(): Astro {
    return Astro(
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

internal fun DayDbo.toDay(): Day {
    return Day(
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
        condition = condition.toCondition(),
    )
}

internal fun DayDto.toDay(): Day {
    return Day(
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
        condition = condition.toCondition(),
    )
}

internal fun AstroDto.toAstro(): Astro {
    return Astro(
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

internal fun CurrentDto.toCurrent(): Current {
    return Current(
        lastUpdatedEpoch = lastUpdatedEpoch,
        tempC = tempC,
        tempF = tempF,
        isDay = isDay,
        condition = condition.toCondition(),
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

internal fun CurrentDbo.toCurrent(): Current {
    return Current(
        lastUpdatedEpoch = lastUpdatedEpoch,
        tempC = tempC,
        tempF = tempF,
        isDay = isDay,
        condition = condition.toCondition(),
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

internal fun HourDto.toHour(): Hour {
    return Hour(
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
        condition = condition.toCondition()
    )
}

internal fun HourDbo.toHour(): Hour {
    return Hour(
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
        condition = condition.toCondition()
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

internal fun ForecastDayDto.toForecastDay(): ForecastDay {
    return ForecastDay(
        dateEpoch = dateEpoch,
        day = day.toDay(),
        astro = astro.toAstro(),
        hour = hour.map { it.toHour() },
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

internal fun ConditionDto.toCondition(): Condition {
    return Condition(
        text = text,
        icon = icon,
        code = code,
    )
}

internal fun ConditionDbo.toCondition(): Condition {
    return Condition(
        text = text,
        icon = icon,
        code = code,
    )
}

internal fun LocationDto.toLocation(): Location {
    return Location(
        id = id.toLong(),
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

internal fun Location.toDbo(): LocationDbo {
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

internal fun LocationDbo.toLocation(): Location {
    return Location(
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
