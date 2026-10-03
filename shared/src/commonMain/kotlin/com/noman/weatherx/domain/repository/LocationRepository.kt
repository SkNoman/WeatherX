package com.noman.weatherx.domain.repository

import com.noman.weatherx.data.model.location.LocationData

interface LocationRepository {
    suspend fun getCurrentLocation(): LocationData?
}