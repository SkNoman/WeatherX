package com.noman.weatherx.data.remote

import com.noman.weatherx.data.model.remote.WeatherDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.URLProtocol
import io.ktor.http.path

class ApiService(private val client: HttpClient) {

    suspend fun getWeatherData(
        latitude: Double,
        longitude: Double,
        currentPresets: String
    ): WeatherDTO{
        return client.get{
            url{
                protocol = URLProtocol.HTTPS
                host = "api.open-meteo.com"
                path("v1","forecast")

                parameter("latitude", latitude)
                parameter("longitude", longitude)
                parameter("current", currentPresets)
                parameter("timezone", "auto")
            }
        }.body()
    }
}