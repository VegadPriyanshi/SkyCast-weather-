package com.pv.skycastweather.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pv.skycastweather.data.local.entity.FavoriteCityEntity

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertFavorite(city: FavoriteCityEntity)

    @Delete
    suspend fun deleteFavorite(city: FavoriteCityEntity)

    @Query("SELECT * FROM favorite_cities ORDER BY addedAt DESC")
    fun getAllFavorites(): LiveData<List<FavoriteCityEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_cities WHERE cityName = :cityName)")
    suspend fun isFavorite(cityName: String): Boolean

}