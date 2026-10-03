package com.noman.weatherx.data.repository

import com.noman.weatherx.data.mappers.toDomain
import com.noman.weatherx.data.remote.ApiService
import com.noman.weatherx.domain.model.Weather
import com.noman.weatherx.domain.repository.WeatherRepository

class WeatherRepoImpl(
    private val apiService: ApiService
) : WeatherRepository {
    override suspend fun getWeather(
        latitude: Double,
        longitude: Double,
        currentPresets: String
    ): Weather {
        return apiService.getWeatherData(
            latitude = latitude,
            longitude = longitude,
            currentPresets = currentPresets
        ).toDomain()
    }
}