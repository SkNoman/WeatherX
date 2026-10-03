package com.noman.weatherx.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.noman.weatherx.ui.permission.RequestLocationPermission
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: HomeViewModel = koinViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var hasLocationPermission by remember { mutableStateOf<Boolean?>(null) }

    RequestLocationPermission { granted ->
        hasLocationPermission = granted
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = uiState.location?.let {
                "Your location\nLatitude: ${it.latitude}\nLongitude: ${it.longitude}"
            } ?: "Location: not available yet",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = uiState.weatherData?.let { weather ->
                "Temperature: ${weather.current.temperature2m}\n" +
                    "Humidity: ${weather.current.relativeHumidity2m}%\n" +
                    "Wind: ${weather.current.windSpeed10m}\n" +
                    "Timezone: ${weather.timezone}"
            } ?: "No weather data yet",
            style = MaterialTheme.typography.titleMedium
        )

        if (hasLocationPermission == false) {
            Text(
                text = "Location permission is required to show your local weather.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        uiState.error?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (uiState.isLoading) {
            CircularProgressIndicator()
        }

        Button(
            onClick = viewModel::getWeatherData,
            enabled = !uiState.isLoading && hasLocationPermission != false
        ) {
            Text("Get Weather")
        }
    }
}
