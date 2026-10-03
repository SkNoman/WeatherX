package com.noman.weatherx.data.model.remote

import com.noman.weatherx.data.model.Current
import kotlinx.serialization.Serializable

@Serializable
data class WeatherDTO(
    val current: Current,
    val current_units: CurrentUnits,
    val elevation: Double,
    val generationtime_ms: Double,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val timezone_abbreviation: String,
    val utc_offset_seconds: Int
)