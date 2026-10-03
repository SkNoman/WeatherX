package com.noman.weatherx.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noman.weatherx.core.WeatherFacade
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val weatherFacade: WeatherFacade
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun getWeatherData() {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val snapshot = weatherFacade.loadWeather()

            _uiState.update { current ->
                if (snapshot.weather != null) {
                    current.copy(
                        isLoading = false,
                        location = snapshot.location,
                        weatherData = snapshot.weather,
                        error = null
                    )
                } else {
                    current.copy(isLoading = false, error = snapshot.error)
                }
            }
        }
    }
}
