package com.mukiva.core.data.repository.forecast.entity

import com.mukiva.core.data.entity.CurrentRemote
import com.mukiva.core.data.entity.LocationRemote

interface ICurrentWeatherRemote {
    val location: LocationRemote?
    val current: CurrentRemote?
}