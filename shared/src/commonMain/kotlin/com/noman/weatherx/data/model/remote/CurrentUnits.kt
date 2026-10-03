package com.noman.weatherx.data.model.remote

import kotlinx.serialization.Serializable

@Serializable
data class CurrentUnits(
    val interval: String,
    val is_day: String,
    val precipitation: String,
    val rain: String,
    val relative_humidity_2m: String,
    val temperature_2m: String,
    val time: String,
    val wind_direction_10m: String,
    val wind_gusts_10m: String,
    val wind_speed_10m: String
)