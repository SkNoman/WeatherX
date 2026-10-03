package com.noman.weatherx

import android.app.Application
import com.noman.weatherx.core.di.dataModule
import com.noman.weatherx.core.di.domainModule
import com.noman.weatherx.core.di.locationModule
import com.noman.weatherx.ui.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BaseApplication)
            modules(dataModule + domainModule + locationModule + viewModelModule)
        }
    }
}
