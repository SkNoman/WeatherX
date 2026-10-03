package com.noman.weatherx

import com.noman.weatherx.core.di.dataModule
import com.noman.weatherx.core.di.domainModule
import com.noman.weatherx.core.di.locationModule
import org.koin.core.context.startKoin

fun initKoin(){
    startKoin {
        modules(dataModule + domainModule + locationModule)
    }
}
