package com.noman.weatherx.data.repository

import com.noman.weatherx.core.di.PlatformLocationManager
import com.noman.weatherx.data.model.location.LocationData
import com.noman.weatherx.domain.repository.LocationRepository

class LocationRepoImpl(
    private val platformLocationManager: PlatformLocationManager
) : LocationRepository {

    override suspend fun getCurrentLocation(): LocationData? =
        platformLocationManager.getCurrentLocation()
}
