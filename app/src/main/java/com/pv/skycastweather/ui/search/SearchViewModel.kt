package com.pv.skycastweather.ui.search

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.pv.skycastweather.data.api.RetrofitClient
import com.pv.skycastweather.data.local.WeatherDatabase
import com.pv.skycastweather.data.model.SearchCity
import com.pv.skycastweather.data.repository.WeatherRepository
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val database = WeatherDatabase.getDatabase(application)

    private val repository = WeatherRepository(
        apiService = RetrofitClient.apiService,
        cachedWeatherDao = database.cachedWeatherDao(),
        cachedHourlyWeatherDao = database.cachedHourlyWeatherDao(),
        cachedForecastDayDao = database.cachedForecastDayDao()
    )

    private val _cities = MutableLiveData<List<SearchCity>>()

    val cities: LiveData<List<SearchCity>>
        get() = _cities

    private val _searchState = MutableLiveData<SearchState>(SearchState.Idle)

    val searchState: LiveData<SearchState>
        get() = _searchState


    private val _selectedCity = MutableLiveData<String>()

    val selectedCity: LiveData<String>
        get() = _selectedCity

   fun searchCities(query: String) {

       if (query.isBlank()) {
           _cities.value = emptyList()
           _searchState.value = SearchState.Idle
           return
       }

       viewModelScope.launch {
           _searchState.value = SearchState.Loading


           try {

               Log.d("SearchVM", "STEP-1 Before repository: $query")

               Log.d("SearchVM", "STEP-2")


               Log.d("SearchVM", "STEP-3")
               val result = repository.searchCities(query)

               Log.d("SearchVM", "Result count = ${result.size}")

               _cities.value = result

               if (result.isEmpty()) {
                   _searchState.value = SearchState.Error("No city found")
               } else {
                   _searchState.value = SearchState.Success
               }
           } catch (e: ConnectException) {

               Log.e("SearchVM", "Connection Error", e)

               _cities.value = emptyList()

               _searchState.value =
                   SearchState.Error("No Internet Connection")

           } catch (e: IOException) {

               Log.e("SearchVM", "IO Error", e)

               _cities.value = emptyList()

               _searchState.value =
                   SearchState.Error("No Internet Connection")
           } catch (e: SocketTimeoutException) {

               Log.e("SearchVM", "Timeout", e)

               _cities.value = emptyList()

               _searchState.value =
                   SearchState.Error("Request timed out. Try again.")

           } catch (e: HttpException) {

               Log.e("SearchVM", "HTTP Error", e)

               _cities.value = emptyList()

               if (e.code() >= 500) {

                   _searchState.value =
                       SearchState.Error("Something went wrong.")

               } else {

                   _searchState.value =
                       SearchState.Error("No city found")

               }

           } catch (t: Throwable) {

               Log.e("SearchVM", "Throwable = ${t.javaClass.name}", t)

               _cities.value = emptyList()

               _searchState.value =
                   SearchState.Error("Throwable: ${t.javaClass.simpleName}")
           }
       }
   }


    fun validateCity(city: String) {

        viewModelScope.launch {

            _searchState.value = SearchState.Loading

            try {

                val response = repository.getCurrentWeather(city)

                if (response.isSuccessful && response.body() != null) {

                    _selectedCity.value = city

                    _searchState.value = SearchState.Success

                } else {

                    _searchState.value =
                        SearchState.Error("No city found")

                }

            } catch (e: UnknownHostException) {

                _searchState.value =
                    SearchState.Error("No Internet Connection")

            } catch (e: ConnectException) {

                Log.e("SearchVM", "Connection Error", e)

                _cities.value = emptyList()

                _searchState.value =
                    SearchState.Error("No Internet Connection")

            } catch (e: IOException) {

                Log.e("SearchVM", "IO Error", e)

                _cities.value = emptyList()

                _searchState.value =
                    SearchState.Error("No Internet Connection")
            } catch (e: HttpException) {

                if (e.code() >= 500) {

                    _searchState.value =
                        SearchState.Error("Something went wrong.")

                } else {

                    _searchState.value =
                        SearchState.Error("No city found")

                }

            } catch (e: Exception) {

                Log.e("SearchVM", "Exception Class = ${e.javaClass.name}")
                Log.e("SearchVM", "Exception Message = ${e.message}", e)

                _cities.value = emptyList()

                _searchState.value =
                    SearchState.Error("Exception: ${e.javaClass.simpleName}")
            }




        }


    }

    fun clearSearchResults() {
        _cities.value = emptyList()
        _searchState.value = SearchState.Idle
    }

}