package com.mukiva.current_weather.domain.model

import com.mukiva.current_weather.domain.model.WindDirection.UNKNOWN

/// See more on:
// https://ambientweather.com/media/wysiwyg/smartwave/porto/ambient/faq/images/compass-rose.jpg
enum class WindDirection(val id: String) {
    UNKNOWN ("UNKNOWN"),
    N       ("N"),
    NNE     ("NNE"),
    NE      ("NE"),
    ENE     ("ENE"),
    E       ("E"),
    ESE     ("ESE"),
    SE      ("SE"),
    SSE     ("SSE"),
    S       ("S"),
    SSW     ("SSW"),
    SW      ("SW"),
    WSW     ("WSW"),
    W       ("W"),
    WNW     ("WNW"),
    NW      ("NW"),
    NNW     ("NNW"),
}
