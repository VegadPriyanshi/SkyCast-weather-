package com.pv.skycastweather.data.model

data class WeatherResponse(

    val location: Location,

    val current: Current,

    val forecast: Forecast

)