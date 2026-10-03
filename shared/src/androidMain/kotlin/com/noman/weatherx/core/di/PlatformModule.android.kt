package com.noman.weatherx.core.di

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.noman.weatherx.data.model.location.LocationData
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.koin.dsl.module

actual val locationModule = module {
    single { PlatformLocationManager(get()) }
}

actual class PlatformLocationManager(
    private val context: Context
) {

    private val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(context)

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    actual suspend fun getCurrentLocation(): LocationData? = withContext(Dispatchers.IO) {

        if (!hasLocationPermission()) {
            Napier.e { "PlatformLocationManager: location permission not granted" }
            return@withContext null
        }

        try {
            fusedLocationClient
                .getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    null
                )
                .await()
                ?.let {
                    Napier.d("PlatformLocationManager: lat=${it.latitude}, lon=${it.longitude}")
                    LocationData(
                        latitude = it.latitude,
                        longitude = it.longitude
                    )
                }
        } catch (e: Exception) {
            Napier.e(e) { "PlatformLocationManager: location request failed" }
            null
        }
    }

    private fun hasLocationPermission(): Boolean =
        context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED ||
            context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED
}
