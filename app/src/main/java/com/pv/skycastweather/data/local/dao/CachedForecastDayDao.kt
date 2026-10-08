package com.pv.skycastweather.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pv.skycastweather.data.local.entity.CachedForecastDayEntity

@Dao
interface CachedForecastDayDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecastDays(forecastDays: List<CachedForecastDayEntity>)

    @Query("SELECT * FROM cached_forecast_day ORDER BY date ASC")
    suspend fun getForecastDays(): List<CachedForecastDayEntity>

    @Query("DELETE FROM cached_forecast_day")
    suspend fun deleteAllForecastDays()

}