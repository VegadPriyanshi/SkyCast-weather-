package com.pv.skycastweather.data.repository

import androidx.lifecycle.LiveData
import com.pv.skycastweather.data.local.dao.SearchHistoryDao
import com.pv.skycastweather.data.local.entity.SearchHistoryEntity

class SearchHistoryRepository(
    private val dao: SearchHistoryDao
) {

    val recentSearches: LiveData<List<SearchHistoryEntity>> =
        dao.getRecentSearches()

    suspend fun insertSearch(search: SearchHistoryEntity) {
        dao.insertAndTrim(search)
    }

    suspend fun deleteSearch(cityName: String) {
        dao.deleteSearch(cityName)
    }

    suspend fun clearAll() {
        dao.clearAll()
    }

    suspend fun getCount(): Int {
        return dao.getCount()
    }
}