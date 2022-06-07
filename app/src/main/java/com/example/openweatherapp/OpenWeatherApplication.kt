package com.example.openweatherapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class OpenWeatherApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}