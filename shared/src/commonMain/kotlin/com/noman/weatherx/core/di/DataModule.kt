package com.noman.weatherx.core.di

import com.noman.weatherx.data.remote.ApiService
import com.noman.weatherx.data.remote.KtorClient
import com.noman.weatherx.data.repository.LocationRepoImpl
import com.noman.weatherx.data.repository.WeatherRepoImpl
import com.noman.weatherx.domain.repository.LocationRepository
import com.noman.weatherx.domain.repository.WeatherRepository
import org.koin.dsl.module

val dataModule = module {
    factory { KtorClient.client }
    factory { ApiService(get()) }
    factory<WeatherRepository> { WeatherRepoImpl(get()) }
    factory<LocationRepository> { LocationRepoImpl(get()) }
}