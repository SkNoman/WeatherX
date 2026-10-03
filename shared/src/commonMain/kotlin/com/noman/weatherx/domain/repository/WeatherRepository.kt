package com.noman.weatherx.domain.repository

import com.noman.weatherx.domain.model.Weather

interface WeatherRepository{
    suspend fun getWeather(
        latitude: Double,
        longitude: Double,
        currentPresets: String
    ): Weather
}