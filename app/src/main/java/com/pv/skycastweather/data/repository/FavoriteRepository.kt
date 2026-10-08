package com.pv.skycastweather.data.repository

import androidx.lifecycle.LiveData
import com.pv.skycastweather.data.local.entity.FavoriteCityEntity
import com.pv.skycastweather.data.local.dao.FavoriteDao

class FavoriteRepository(
    private val favoriteDao: FavoriteDao
) {

    val allFavorites: LiveData<List<FavoriteCityEntity>> =
        favoriteDao.getAllFavorites()

    suspend fun insert(city: FavoriteCityEntity) {
        favoriteDao.insertFavorite(city)
    }

    suspend fun delete(city: FavoriteCityEntity) {
        favoriteDao.deleteFavorite(city)
    }

    suspend fun isFavorite(cityName: String): Boolean {
        return favoriteDao.isFavorite(cityName)
    }
}