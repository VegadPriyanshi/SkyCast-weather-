package com.pv.skycastweather.ui.search

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.pv.skycastweather.data.local.entity.SearchHistoryEntity
import com.pv.skycastweather.data.local.WeatherDatabase
import com.pv.skycastweather.data.repository.SearchHistoryRepository
import kotlinx.coroutines.launch

class SearchHistoryViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository: SearchHistoryRepository

    val recentSearches: LiveData<List<SearchHistoryEntity>>

    init {

        val dao = WeatherDatabase
            .getDatabase(application)
            .searchHistoryDao()

        repository = SearchHistoryRepository(dao)

        recentSearches = repository.recentSearches
    }

    fun insertSearch(cityName: String) {

        viewModelScope.launch {

            repository.insertSearch(
                SearchHistoryEntity(
                    cityName = cityName
                )
            )

        }

    }

    fun deleteSearch(cityName: String) {

        viewModelScope.launch {

            repository.deleteSearch(cityName)

        }

    }

    fun clearAll() {

        viewModelScope.launch {

            Log.d("RoomTest", "Before = ${repository.getCount()}")

            repository.clearAll()

            Log.d("RoomTest", "After = ${repository.getCount()}")

        }

    }

}