package com.github.mukiva.open_weather.core.domain.weather

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