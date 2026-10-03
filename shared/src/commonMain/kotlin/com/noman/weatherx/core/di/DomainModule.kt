package com.noman.weatherx.core.di

import com.noman.weatherx.core.WeatherFacade
import com.noman.weatherx.domain.usecases.GetWeatherUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetWeatherUseCase(get()) }
    factory { WeatherFacade(get(), get()) }
}
