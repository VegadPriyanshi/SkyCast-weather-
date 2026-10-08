package com.pv.skycastweather.data.mapper

import com.pv.skycastweather.data.model.WeatherResponse
import com.pv.skycastweather.data.model.WeatherUiModel

object WeatherMapper {

    fun toUiModel(weather: WeatherResponse): WeatherUiModel {

        return WeatherUiModel(

            city = weather.location.name,
            region = weather.location.region,
            country = weather.location.country,

            temperature = weather.current.temp_c,

            feelsLike = weather.current.feelslike_c,

            condition = weather.current.condition.text,

            iconUrl = "https:${weather.current.condition.icon}",

            iconLocalPath = null,

            humidity = weather.current.humidity,

            windSpeed = weather.current.wind_kph,

            pressure = weather.current.pressure_mb,

            visibility = weather.current.vis_km,

            uvIndex = weather.current.uv,

            isDay = weather.current.is_day,

            lastUpdated = weather.current.last_updated,

            latitude = weather.location.lat,

            longitude = weather.location.lon
        )
    }

}
