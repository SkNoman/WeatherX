package com.noman.weatherx.domain.model

data class CurrentWeather(
    val interval: Int,
    val isDay: Boolean,
    val precipitation: Double,
    val rain: Double,
    val relativeHumidity2m: Int,
    val temperature2m: Double,
    val time: String,
    val windDirection10m: Int,
    val windGusts10m: Double,
    val windSpeed10m: Double
)
