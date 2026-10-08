package com.pv.skycastweather.data.model

data class Day(

    val maxtemp_c: Double,

    val mintemp_c: Double,

    val daily_chance_of_rain: Int,

    val condition: Condition

)