package com.pv.skycastweather.data.model

data class WeatherUiModel(

    val city: String,

    val region: String,

    val country: String,

    val temperature: Double,

    val feelsLike: Double,

    val condition: String,

    val iconUrl: String,

    val iconLocalPath: String? = null,

    val humidity: Int,

    val windSpeed: Double,

    val pressure: Double,

    val visibility: Double,

    val uvIndex: Double,

    val isDay: Int,

    val lastUpdated: String,

    val latitude: Double,

    val longitude: Double

)