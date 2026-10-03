package com.noman.weatherx.ui.home

import com.noman.weatherx.data.model.location.LocationData
import com.noman.weatherx.domain.model.Weather

data class HomeUiState(
    val isLoading: Boolean = false,
    val location: LocationData? = null,
    val weatherData: Weather? = null,
    val error: String? = null
)
