package com.pv.skycastweather.data.api

import com.pv.skycastweather.data.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import com.pv.skycastweather.data.model.SearchCity
interface ApiService {

    @GET("forecast.json")
    suspend fun getCurrentWeather(

        @Query("key")
        apiKey: String,

        @Query("q")
        city: String,

        @Query("days")
        days: Int = 7

    ): Response<WeatherResponse>

    @GET("search.json")
    suspend fun searchCities(

        @Query("key") apiKey: String,

        @Query("q") query: String

    ): List<SearchCity>
}