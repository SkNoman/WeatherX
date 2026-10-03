package com.noman.weatherx.data.mappers

import com.noman.weatherx.data.model.Current
import com.noman.weatherx.data.model.remote.WeatherDTO
import com.noman.weatherx.domain.model.CurrentWeather
import com.noman.weatherx.domain.model.Weather

fun Current.toDomain(): CurrentWeather = CurrentWeather(
    interval = interval,
    isDay = is_day == 1,
    precipitation = precipitation,
    rain = rain,
    relativeHumidity2m = relative_humidity_2m,
    temperature2m = temperature_2m,
    time = time,
    windDirection10m = wind_direction_10m,
    windGusts10m = wind_gusts_10m,
    windSpeed10m = wind_speed_10m
)

fun WeatherDTO.toDomain(): Weather = Weather(
    current = current.toDomain(),
    elevation = elevation,
    generationTimeMs = generationtime_ms,
    latitude = latitude,
    longitude = longitude,
    timezone = timezone,
    timezoneAbbreviation = timezone_abbreviation,
    utcOffsetSeconds = utc_offset_seconds
)
