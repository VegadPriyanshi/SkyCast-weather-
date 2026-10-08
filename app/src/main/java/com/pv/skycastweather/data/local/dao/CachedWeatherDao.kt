package com.pv.skycastweather.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pv.skycastweather.data.local.entity.CachedWeatherEntity

@Dao
interface CachedWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveWeather(weather: CachedWeatherEntity)

    @Query("SELECT * FROM cached_weather WHERE id = 1")
    suspend fun getCachedWeather(): CachedWeatherEntity?

}