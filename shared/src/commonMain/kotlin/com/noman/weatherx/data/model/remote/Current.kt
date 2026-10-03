package com.noman.weatherx.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Current(
    val interval: Int,
    val is_day: Int,
    val precipitation: Double,
    val rain: Double,
    val relative_humidity_2m: Int,
    val temperature_2m: Double,
    val time: String,
    val wind_direction_10m: Int,
    val wind_gusts_10m: Double,
    val wind_speed_10m: Double
)