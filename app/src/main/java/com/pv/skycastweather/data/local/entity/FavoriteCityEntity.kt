package com.pv.skycastweather.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_cities")
data class FavoriteCityEntity(

    @PrimaryKey
    val cityName: String,

    val addedAt: Long = System.currentTimeMillis()

)