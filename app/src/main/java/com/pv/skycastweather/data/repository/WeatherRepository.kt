package com.pv.skycastweather.data.repository

import android.content.Context
import android.util.Log
import com.pv.skycastweather.BuildConfig
import com.pv.skycastweather.data.api.ApiService
import com.pv.skycastweather.data.api.RetrofitClient
import com.pv.skycastweather.data.local.dao.CachedForecastDayDao
import com.pv.skycastweather.data.local.dao.CachedHourlyWeatherDao
import com.pv.skycastweather.data.local.dao.CachedWeatherDao
import com.pv.skycastweather.data.local.entity.CachedForecastDayEntity
import com.pv.skycastweather.data.local.entity.CachedHourlyWeatherEntity
import com.pv.skycastweather.data.local.entity.CachedWeatherEntity
import retrofit2.Response
import com.pv.skycastweather.data.model.WeatherResponse
import com.pv.skycastweather.data.model.SearchCity
import com.pv.skycastweather.data.model.WeatherUiModel
import com.pv.skycastweather.data.model.Condition
import com.pv.skycastweather.data.model.Current
import com.pv.skycastweather.data.model.Day
import com.pv.skycastweather.data.model.ForecastDay
import com.pv.skycastweather.data.model.Hour
import com.pv.skycastweather.data.model.Location
import com.pv.skycastweather.utils.ImageCacheHelper

class WeatherRepository(

    private val apiService: ApiService,

    private val cachedWeatherDao: CachedWeatherDao,

    private val cachedHourlyWeatherDao: CachedHourlyWeatherDao,

    private val cachedForecastDayDao: CachedForecastDayDao

){

    suspend fun getCurrentWeather(city: String): Response<WeatherResponse> {

        val response = apiService.getCurrentWeather(
            apiKey = BuildConfig.WEATHER_API_KEY,
            city = city
        )
        if (response.isSuccessful) {
            val forecastDays = response.body()?.forecast?.forecastday

            Log.d("WeatherAPI", "Forecast size = ${forecastDays?.size}")

            forecastDays?.forEachIndexed { index, day ->
                Log.d("WeatherAPI", "Day $index = ${day.date}")
            }
        }

        val forecastDays = response.body()?.forecast?.forecastday

        Log.d("WeatherAPI", "Forecast size = ${forecastDays?.size}")

        forecastDays?.forEachIndexed { index, day ->
            Log.d("WeatherAPI", "Day $index = ${day.date}")
        }

        Log.d("WeatherURL", response.raw().request.url.toString())
        Log.d("WeatherCode", "HTTP = ${response.code()}")
        Log.d(
            "WeatherAPI",
            "API forecast size = ${response.body()?.forecast?.forecastday?.size}"
        )

        response.body()?.forecast?.forecastday?.forEachIndexed { index, day ->
            Log.d("WeatherAPI", "Day $index = ${day.date}")
        }

        if (response.isSuccessful) {
            response.body()?.let { weather ->

                // Save current weather
                cachedWeatherDao.saveWeather(
                    weather.toCachedEntity()
                )

                // Remove old hourly forecast
                cachedHourlyWeatherDao.deleteAllHourlyWeather()

                // Save latest hourly forecast
                cachedHourlyWeatherDao.insertHourlyWeather(
                    weather.toCachedHourlyEntities()
                )

                // Save forecast days
                cachedForecastDayDao.deleteAllForecastDays()

                val forecastEntities = weather.toCachedForecastEntities()

                Log.d("ForecastCache", "Forecast from API = ${forecastEntities.size}")

                cachedForecastDayDao.insertForecastDays(forecastEntities)

                Log.d(
                    "ForecastCache",
                    "Forecast in DB after insert = ${cachedForecastDayDao.getForecastDays().size}"
                )

            }
        }
        return response
    }

    /*suspend fun searchCities(
        query: String
    ): List<SearchCity> {

        return apiService.searchCities(
            BuildConfig.WEATHER_API_KEY,
            query
        )

    }*/

    suspend fun searchCities(
        query: String
    ): List<SearchCity> {

        Log.d("Repository", "Before API Call")

        val result = apiService.searchCities(
            BuildConfig.WEATHER_API_KEY,
            query
        )

        Log.d("Repository", "After API Call")

        return result
    }

    suspend fun saveCachedWeather(weather: CachedWeatherEntity) {

        cachedWeatherDao.saveWeather(weather)

    }

    suspend fun getCachedWeather(): WeatherUiModel? {

        return cachedWeatherDao
            .getCachedWeather()
            ?.toUiModel()

    }

    suspend fun getCachedHourlyWeather(): List<Hour> {

        return cachedHourlyWeatherDao
            .getHourlyWeather()
            .map { entity ->

                Hour(

                    time = entity.time,

                    temp_c = entity.temperature,

                    condition = Condition(

                        text = entity.condition,

                        icon = entity.iconUrl,

                        code = 0

                    )

                )

            }

    }

    suspend fun getCachedForecastDays(): List<ForecastDay> {

        val cachedForecasts = cachedForecastDayDao.getForecastDays()

        Log.d("ForecastCache", "Cached forecast count = ${cachedForecasts.size}")

        return cachedForecastDayDao.getForecastDays().map { cached ->
            ForecastDay(

                date = cached.date,

                day = Day(

                    maxtemp_c = cached.maxTemp,

                    mintemp_c = cached.minTemp,

                    daily_chance_of_rain = 0,

                    condition = Condition(

                        text = cached.condition,

                        icon = cached.iconUrl,

                        code = 0

                    )

                ),

                hour = emptyList()

            )

        }

    }
    private fun CachedWeatherEntity.toUiModel(): WeatherUiModel {

        return WeatherUiModel(

            city = city,

            region = region,

            country = country,

            temperature = temperature,

            feelsLike = feelsLike,

            condition = condition,

            iconUrl = iconUrl,
            iconLocalPath = iconLocalPath,

            humidity = humidity,

            windSpeed = windSpeed,

            pressure = pressure,

            visibility = visibility,

            uvIndex = uvIndex,

            isDay = isDay,

            lastUpdated = lastUpdated,

            latitude = latitude,

            longitude = longitude

        )
    }

    private fun WeatherResponse.toCachedEntity(): CachedWeatherEntity {

        return CachedWeatherEntity(

            id = 1,

            city = location.name,

            region = location.region,

            country = location.country,

            temperature = current.temp_c,

            feelsLike = current.feelslike_c,

            condition = current.condition.text,

            iconUrl = current.condition.icon,

            humidity = current.humidity,

            windSpeed = current.wind_kph,

            pressure = current.pressure_mb,

            visibility = current.vis_km,

            uvIndex = current.uv,

            isDay = current.is_day,

            lastUpdated = current.last_updated,

            latitude = location.lat,

            longitude = location.lon

        )
    }

    private fun WeatherResponse.toCachedHourlyEntities():
            List<CachedHourlyWeatherEntity> {

        return forecast.forecastday.first().hour.map { hour ->

            CachedHourlyWeatherEntity(

                time = hour.time,

                temperature = hour.temp_c,

                condition = hour.condition.text,

                iconUrl = hour.condition.icon,

                chanceOfRain = 0

            )

        }

    }

    private fun WeatherResponse.toCachedForecastEntities():
            List<CachedForecastDayEntity> {

        return forecast.forecastday.map { forecastDay ->

            CachedForecastDayEntity(

                date = forecastDay.date,

                maxTemp = forecastDay.day.maxtemp_c,

                minTemp = forecastDay.day.mintemp_c,

                condition = forecastDay.day.condition.text,

                iconUrl = forecastDay.day.condition.icon

            )

        }

    }
}