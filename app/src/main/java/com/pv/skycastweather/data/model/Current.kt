package com.pv.skycastweather.data.model

data class Current(

    val temp_c: Double,

    val feelslike_c: Double,

    val humidity: Int,

    val wind_kph: Double,

    val pressure_mb: Double,

    val vis_km: Double,

    val uv: Double,

    val condition: Condition,
    val is_day: Int,
    val last_updated: String,

    val lat: Double,

    val lon: Double,


)