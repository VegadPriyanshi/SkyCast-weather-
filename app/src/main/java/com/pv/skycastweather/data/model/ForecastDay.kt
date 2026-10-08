package com.pv.skycastweather.data.model

data class ForecastDay(

    val date: String,

    val day: Day,

    val hour: List<Hour>

)