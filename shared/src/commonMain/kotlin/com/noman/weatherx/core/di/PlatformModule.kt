package com.noman.weatherx.core.di

import com.noman.weatherx.data.model.location.LocationData
import org.koin.core.module.Module

expect class PlatformLocationManager {
    suspend fun getCurrentLocation(): LocationData?
}

expect val locationModule: Module
