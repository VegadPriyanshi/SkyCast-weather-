package com.pv.skycastweather.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_hourly_weather")
data class CachedHourlyWeatherEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val time: String,

    val temperature: Double,

    val condition: String,

    val iconUrl: String,

    val chanceOfRain: Int

)