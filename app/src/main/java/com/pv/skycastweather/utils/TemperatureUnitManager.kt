package com.pv.skycastweather.utils

import android.content.Context

object TemperatureUnitManager {

    private const val PREF_NAME = "app_settings"
    private const val KEY_TEMPERATURE_UNIT = "temperature_unit"

    const val CELSIUS = "celsius"
    const val FAHRENHEIT = "fahrenheit"

    fun saveTemperatureUnit(
        context: Context,
        unit: String
    ) {
        val prefs =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        prefs.edit()
            .putString(KEY_TEMPERATURE_UNIT, unit)
            .apply()
    }

    fun getTemperatureUnit(context: Context): String {

        val prefs =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        return prefs.getString(
            KEY_TEMPERATURE_UNIT,
            CELSIUS
        ) ?: CELSIUS
    }

    fun isCelsius(context: Context): Boolean {

        return getTemperatureUnit(context) == CELSIUS

    }

    fun isFahrenheit(context: Context): Boolean {

        return getTemperatureUnit(context) == FAHRENHEIT

    }
}