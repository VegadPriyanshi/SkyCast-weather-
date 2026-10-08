package com.pv.skycastweather.utils

import com.pv.skycastweather.R

object WeatherBackgroundManager {

    fun getBackground(
        conditionCode: Int,
        isDay: Int
    ): Int {

        // Night has highest priority
        if (isDay == 0) {
            return R.drawable.bg_night
        }

        return when (conditionCode) {

            // Sunny
            1000 -> R.drawable.bg_sunny

            // Partly Cloudy
            1003 -> R.drawable.bg_cloudy

            // Cloudy
            1006,
            1009 -> R.drawable.bg_cloudy

            // Rain
            1063,
            1150,
            1153,
            1180,
            1183,
            1186,
            1189,
            1192,
            1195,
            1240,
            1243,
            1246 -> R.drawable.bg_rain

            else -> R.drawable.bg_cloudy
        }
    }
}