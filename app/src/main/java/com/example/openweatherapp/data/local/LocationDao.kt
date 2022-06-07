package com.example.openweatherapp.data.local

import androidx.room.*
import com.example.openweatherapp.data.local.Location.Companion.TABLE_NAME

/**
 * Data access object (DAO) interface which provides methods
 * to interact with table
 */
@Dao
interface LocationDao {

    @Query("SELECT * FROM $TABLE_NAME WHERE location_name LIKE :locationName")
    suspend fun getCityWeatherFromDB(locationName: String) :Location

    @Query("SELECT location_name FROM $TABLE_NAME")
    suspend fun getAllBookmarkLocation() : List<String>

    @Query("SELECT EXISTS(SELECT * FROM $TABLE_NAME WHERE id = :id)")
    suspend fun isBookMarkPresent(id: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: Location): Long

    @Delete
    suspend fun removeFromBookMark(location: Location): Int
}