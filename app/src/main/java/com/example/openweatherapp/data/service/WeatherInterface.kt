package com.example.openweatherapp.data.service

import com.example.openweatherapp.utils.API_KEY
import com.example.openweatherapp.utils.UNIT
import com.example.openweatherapp.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherInterface {

    @GET("weather")
    suspend fun getCityWeather(
        @Query("q") q: String,
        @Query("units") units: String = UNIT,
        @Query("appid") appid: String = API_KEY
    ): WeatherResponse
}