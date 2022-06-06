package com.example.openweatherapp.data.repositories

import com.example.openweatherapp.data.local.Location
import com.example.openweatherapp.data.local.WeatherDatabase
import com.example.openweatherapp.data.model.WeatherResponse
import com.example.openweatherapp.data.service.WeatherInterface
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val service: WeatherInterface, private val db: WeatherDatabase) {

    suspend fun getCityWeather(city: String): WeatherResponse {
        return service.getCityWeather(city)
    }

    suspend fun getAllBookmarkLocation(): List<String> {
        return db.locationDao().getAllBookmarkLocation()
    }

    suspend fun addToBookMark(location: Location): Long {
        return db.locationDao().insertLocation(location)
    }

    suspend fun removeFromBookMark(location: Location): Int {
        return db.locationDao().removeFromBookMark(location)
    }

    suspend fun isBookMarkPresent(id: Int): Boolean {
        return db.locationDao().isBookMarkPresent(id)
    }
}