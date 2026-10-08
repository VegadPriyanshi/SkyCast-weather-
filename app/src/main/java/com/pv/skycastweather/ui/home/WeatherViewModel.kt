package com.pv.skycastweather.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pv.skycastweather.data.model.WeatherResponse
import com.pv.skycastweather.data.repository.WeatherRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.pv.skycastweather.data.api.RetrofitClient
import com.pv.skycastweather.data.local.WeatherDatabase
import com.pv.skycastweather.data.model.ForecastDay
import com.pv.skycastweather.data.model.Hour
import com.pv.skycastweather.data.model.WeatherUiModel

class WeatherViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val database =
        WeatherDatabase.getDatabase(application)

    private val repository = WeatherRepository(
        apiService = RetrofitClient.apiService,
        cachedWeatherDao = database.cachedWeatherDao(),
        cachedHourlyWeatherDao = database.cachedHourlyWeatherDao(),
        cachedForecastDayDao = database.cachedForecastDayDao()
    )
    private val _weather = MutableLiveData<Response<WeatherResponse>>()

    private val _cachedWeather = MutableLiveData<WeatherUiModel?>()

    private val _cachedHourlyWeather = MutableLiveData<List<Hour>>()

    private val _cachedForecastDays = MutableLiveData<List<ForecastDay>>()

    val cachedWeather: LiveData<WeatherUiModel?>
        get() = _cachedWeather

    val cachedHourlyWeather: LiveData<List<Hour>>
        get() = _cachedHourlyWeather

    val cachedForecastDays: LiveData<List<ForecastDay>>
        get() = _cachedForecastDays

    val weather: LiveData<Response<WeatherResponse>>
        get() = _weather



    fun getCurrentWeather(city: String) {

        viewModelScope.launch {

            val response = repository.getCurrentWeather(city)

            _weather.value = response



        }

    }

    fun loadCachedWeather() {

        viewModelScope.launch {

            _cachedWeather.value = repository.getCachedWeather()

        }

    }

    fun loadCachedHourlyWeather() {

        viewModelScope.launch {

            _cachedHourlyWeather.value =
                repository.getCachedHourlyWeather()

        }

    }

    fun loadCachedForecastDays() {

        viewModelScope.launch {

            _cachedForecastDays.value =
                repository.getCachedForecastDays()

        }

    }

}