package com.pv.skycastweather.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.pv.skycastweather.data.local.entity.SearchHistoryEntity

@Dao
interface SearchHistoryDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertSearch(search: SearchHistoryEntity)

    @Query("""
    SELECT *
    FROM search_history
    ORDER BY timestamp DESC
    LIMIT 10
""")
    fun getRecentSearches(): LiveData<List<SearchHistoryEntity>>

    @Query("DELETE FROM search_history WHERE cityName = :cityName")
    suspend fun deleteSearch(cityName: String)


    @Query("SELECT COUNT(*) FROM search_history")
    suspend fun getCount(): Int


    @Query("DELETE FROM search_history")
    suspend fun clearAll()

    @Query("""
    DELETE FROM search_history
    WHERE cityName NOT IN (
        SELECT cityName
        FROM search_history
        ORDER BY timestamp DESC
        LIMIT 10
    )
""")
    suspend fun deleteOldSearches()

    @Transaction
    suspend fun insertAndTrim(search: SearchHistoryEntity) {
        insertSearch(search)
        deleteOldSearches()
    }

}