package com.pv.skycastweather.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pv.skycastweather.data.local.dao.CachedForecastDayDao
import com.pv.skycastweather.data.local.dao.CachedHourlyWeatherDao
import com.pv.skycastweather.data.local.dao.CachedWeatherDao
import com.pv.skycastweather.data.local.dao.FavoriteDao
import com.pv.skycastweather.data.local.dao.SearchHistoryDao
import com.pv.skycastweather.data.local.entity.CachedForecastDayEntity
import com.pv.skycastweather.data.local.entity.CachedHourlyWeatherEntity
import com.pv.skycastweather.data.local.entity.CachedWeatherEntity
import com.pv.skycastweather.data.local.entity.FavoriteCityEntity
import com.pv.skycastweather.data.local.entity.SearchHistoryEntity

@Database(
    entities = [
        SearchHistoryEntity::class,
        FavoriteCityEntity::class,
        CachedWeatherEntity::class,
        CachedForecastDayEntity::class,
        CachedHourlyWeatherEntity::class
    ],
    version = 6,
    exportSchema = false
)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun searchHistoryDao(): SearchHistoryDao

    abstract fun favoriteDao(): FavoriteDao

    abstract fun cachedWeatherDao(): CachedWeatherDao

    abstract fun cachedHourlyWeatherDao(): CachedHourlyWeatherDao

    abstract fun cachedForecastDayDao(): CachedForecastDayDao

    companion object {

        @Volatile
        private var INSTANCE: WeatherDatabase? = null

        fun getDatabase(context: Context): WeatherDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDatabase::class.java,
                    "weather_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance

                instance
            }
        }
    }
}