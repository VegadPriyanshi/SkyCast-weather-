package com.pv.skycastweather.utils

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

object ThemeManager {

    private const val PREF_NAME = "app_settings"
    private const val KEY_THEME = "theme"

    const val SYSTEM = "system"
    const val LIGHT = "light"
    const val DARK = "dark"

    fun saveTheme(context: Context, theme: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_THEME, theme).apply()
    }

    fun getTheme(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_THEME, SYSTEM) ?: SYSTEM
    }

    fun applyTheme(theme: String) {
        when (theme) {
            LIGHT -> AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO
            )

            DARK -> AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES
            )

            else -> AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            )
        }
    }
}