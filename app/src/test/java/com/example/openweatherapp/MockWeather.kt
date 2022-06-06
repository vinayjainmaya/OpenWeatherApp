package com.example.openweatherapp

import com.example.openweatherapp.data.model.WeatherResponse


val mockCityWeather = WeatherResponse(
    base = "stations",
    visibility = 10000,
    dt = 1654412792,
    timezone = 7200,
    id = 2759794,
    name = "Amsterdam",
    cod = 200,
    coord = WeatherResponse.Coord(
        lat = 4.8897,
        lon = 52.374
    ),
    main = WeatherResponse.Main(
        temp = 13.98,
        feelsLike = 13.65,
        tempMin = 12.71,
        tempMax = 14.5,
        pressure = 1011,
        humidity = 85,
        grndLevel = 43,
        seaLevel = 8797,
    ),
    wind = WeatherResponse.Wind(
        speed = 6.17,
        deg = 70,
        gust = 898.7
    ),
    clouds = WeatherResponse.Clouds(
        all = 20
    ),
    sys = WeatherResponse.Sys(
        type = 2,
        id = 2046553,
        country = "NL",
        sunrise = 1654399302,
        sunset = 1654458985
    ),
    weather = listOf(
        WeatherResponse.Weather(
            id = 801,
            main = "Clouds",
            description = "few clouds",
            icon = "02d"
        )
    ),
    rain = WeatherResponse.Rain(
        oneH = 0.11,
        threeH = 0.10
    ),
    snow = WeatherResponse.Snow(
        oneH = 0.11,
        threeH = 0.10
    )
)