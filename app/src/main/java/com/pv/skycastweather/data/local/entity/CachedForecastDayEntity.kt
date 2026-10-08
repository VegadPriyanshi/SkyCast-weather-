package com.pv.skycastweather.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_forecast_day")
data class CachedForecastDayEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val date: String,

    val maxTemp: Double,

    val minTemp: Double,

    val condition: String,

    val iconUrl: String

)