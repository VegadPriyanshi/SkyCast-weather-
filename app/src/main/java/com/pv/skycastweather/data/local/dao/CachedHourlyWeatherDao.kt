package com.pv.skycastweather.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import com.pv.skycastweather.data.local.entity.CachedHourlyWeatherEntity
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CachedHourlyWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHourlyWeather(hourlyWeather: List<CachedHourlyWeatherEntity>)

    @Query("SELECT * FROM cached_hourly_weather ORDER BY time ASC")
    suspend fun getHourlyWeather(): List<CachedHourlyWeatherEntity>

    @Query("DELETE FROM cached_hourly_weather")
    suspend fun deleteAllHourlyWeather()

}