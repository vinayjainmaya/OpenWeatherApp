package com.example.openweatherapp.data.service

import com.example.openweatherapp.utils.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class APIManager {

    val retrofit: Retrofit

    init {
        val okkHttpclient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        retrofit = Retrofit.Builder()
            .client(okkHttpclient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    companion object {
        private var instance: APIManager? = null

        @Synchronized
        fun getInstance(): APIManager {
            if (instance == null) {
                instance = APIManager()
            }
            return instance as APIManager
        }
    }
}