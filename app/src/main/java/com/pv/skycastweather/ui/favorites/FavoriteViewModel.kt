package com.pv.skycastweather.ui.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.pv.skycastweather.data.local.entity.FavoriteCityEntity
import com.pv.skycastweather.data.local.WeatherDatabase
import com.pv.skycastweather.data.repository.FavoriteRepository
import kotlinx.coroutines.launch
import androidx.lifecycle.MutableLiveData
import com.pv.skycastweather.data.api.RetrofitClient
import com.pv.skycastweather.data.model.FavoriteWeather
import com.pv.skycastweather.data.repository.WeatherRepository

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FavoriteRepository

    val allFavorites: LiveData<List<FavoriteCityEntity>>

    private val database = WeatherDatabase.getDatabase(application)

    private val weatherRepository = WeatherRepository(
        apiService = RetrofitClient.apiService,
        cachedWeatherDao = database.cachedWeatherDao(),
        cachedHourlyWeatherDao = database.cachedHourlyWeatherDao(),
        cachedForecastDayDao = database.cachedForecastDayDao()
    )
    val favoriteWeatherList = MutableLiveData<List<FavoriteWeather>>()

    init {

        val favoriteDao =
            WeatherDatabase.getDatabase(application).favoriteDao()

        repository = FavoriteRepository(favoriteDao)

        allFavorites = repository.allFavorites

        allFavorites.observeForever { favorites ->

            loadFavoriteWeather(favorites)

        }

    }

    fun insert(cityName: String) {

        viewModelScope.launch {

            repository.insert(
                FavoriteCityEntity(cityName)
            )

        }

    }

    fun delete(cityName: String) {

        viewModelScope.launch {

            repository.delete(
                FavoriteCityEntity(cityName)
            )

        }

    }
    private fun loadFavoriteWeather(
        favorites: List<FavoriteCityEntity>
    ) {

        viewModelScope.launch {

            val weatherList = mutableListOf<FavoriteWeather>()

            favorites.forEach { favorite ->

                val response =
                    weatherRepository.getCurrentWeather(favorite.cityName)

                if (response.isSuccessful && response.body() != null) {

                    val weather = response.body()!!

                    weatherList.add(

                        FavoriteWeather(

                            cityName = weather.location.name,

                            temperature = "${weather.current.temp_c.toInt()}°",

                            condition = weather.current.condition.text,

                            iconUrl = "https:${weather.current.condition.icon}"

                        )

                    )

                }

            }

            favoriteWeatherList.postValue(weatherList)

        }

    }

    suspend fun isFavorite(cityName: String): Boolean {

        return repository.isFavorite(cityName)

    }

}