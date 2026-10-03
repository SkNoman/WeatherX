package com.noman.weatherx.domain.usecases

import com.noman.weatherx.domain.model.Weather
import com.noman.weatherx.domain.repository.WeatherRepository

class GetWeatherUseCase(private val weatherRepository: WeatherRepository) {

    suspend fun getWeatherData(
        latitude: Double,
        longitude: Double,
        currentPresets: String
    ): Result<Weather>{
        return try {
            val response = weatherRepository.getWeather(
                latitude = latitude,
                longitude = longitude,
                currentPresets = currentPresets
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}