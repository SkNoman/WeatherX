package com.noman.weatherx.core

import org.koin.core.component.KoinComponent
import org.koin.core.component.get

/**
 * Entry point for the Swift UI, which cannot use Koin directly.
 *
 * From Swift: `let facade = WeatherProvider.shared.weatherFacade()`
 */
object WeatherProvider : KoinComponent {

    fun weatherFacade(): WeatherFacade = get()
}
