package com.example.openweatherapp.di

import android.content.Context
import com.example.openweatherapp.data.local.WeatherDatabase
import com.example.openweatherapp.data.repositories.WeatherRepository
import com.example.openweatherapp.data.service.WeatherInterface
import com.example.openweatherapp.ui.viewmodel.WeatherViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit

@Module
@InstallIn(ActivityRetainedComponent::class)
object WeatherModule {

    @Provides
    fun provideWeatherViewModel(repository: WeatherRepository) = WeatherViewModel(repository)

    @Provides
    fun provideWeatherRepository(service: WeatherInterface, db: WeatherDatabase) = WeatherRepository(service,db)

    @Provides
    fun provideWeatherInterface(retrofit: Retrofit) = retrofit.create(WeatherInterface::class.java)

}