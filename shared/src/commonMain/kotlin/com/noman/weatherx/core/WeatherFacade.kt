package com.noman.weatherx.core

import com.noman.weatherx.data.model.location.LocationData
import com.noman.weatherx.domain.model.Weather
import com.noman.weatherx.domain.repository.LocationRepository
import com.noman.weatherx.domain.usecases.GetWeatherUseCase
import io.github.aakira.napier.Napier

/**
 * Platform agnostic snapshot of the home screen data.
 *
 * Kept free of Result and Flow types so it maps cleanly onto Swift as well.
 */
data class WeatherSnapshot(
    val location: LocationData? = null,
    val weather: Weather? = null,
    val error: String? = null
)

/**
 * Reads the current location and loads the weather for it.
 *
 * Shared by the Android and the iOS home screens so neither platform has to
 * duplicate the location -> weather orchestration.
 */
class WeatherFacade(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val locationRepository: LocationRepository
) {

    suspend fun loadWeather(): WeatherSnapshot {
        val location = locationRepository.getCurrentLocation()
            ?: return WeatherSnapshot(error = LOCATION_ERROR)

        Napier.d { "WeatherFacade: lat=${location.latitude}, lon=${location.longitude}" }

        return getWeatherUseCase
            .getWeatherData(
                latitude = location.latitude,
                longitude = location.longitude,
                currentPresets = CURRENT_PRESETS
            )
            .fold(
                onSuccess = { WeatherSnapshot(location = location, weather = it) },
                onFailure = {
                    Napier.e(it) { "WeatherFacade: weather request failed" }
                    WeatherSnapshot(location = location, error = it.message ?: UNKNOWN_ERROR)
                }
            )
    }

    private companion object {
        const val CURRENT_PRESETS =
            "temperature_2m,precipitation,rain,relative_humidity_2m,is_day,wind_speed_10m,wind_direction_10m,wind_gusts_10m"
        const val LOCATION_ERROR = "Could not get your location"
        const val UNKNOWN_ERROR = "Unknown error"
    }
}
