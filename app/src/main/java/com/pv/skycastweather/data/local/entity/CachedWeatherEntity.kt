package com.pv.skycastweather.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_weather")

data class CachedWeatherEntity(@PrimaryKey
                               val id: Int = 1,
                               val city: String,
                               val region: String,
                               val country: String,

                               val temperature: Double,
                               val feelsLike: Double,

                               val condition: String,
                               val iconUrl: String,

                               val humidity: Int,
                               val windSpeed: Double,
                               val pressure: Double,
                               val visibility: Double,
                               val uvIndex: Double,

                               val isDay: Int,

                               val lastUpdated: String,

                               val latitude: Double,
                               val longitude: Double,
                               val iconLocalPath: String? = null)
