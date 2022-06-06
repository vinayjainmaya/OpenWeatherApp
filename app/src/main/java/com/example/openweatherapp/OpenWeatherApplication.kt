package com.example.openweatherapp

import android.app.Application
import com.example.openweatherapp.data.local.WeatherDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class OpenWeatherApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        val database by lazy { WeatherDatabase.getDatabase(this) }
//        val repository by lazy { WordRepository(database.wordDao()) }
    }
}