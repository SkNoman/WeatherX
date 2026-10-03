package com.noman.weatherx.core.di

import com.noman.weatherx.data.model.location.LocationData
import io.github.aakira.napier.Napier
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import org.koin.dsl.module
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusDenied
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.CoreLocation.kCLAuthorizationStatusRestricted
import platform.CoreLocation.kCLLocationAccuracyHundredMeters
import platform.Foundation.NSError
import platform.darwin.NSObject
import kotlin.coroutines.resume
import kotlin.time.Duration.Companion.milliseconds

actual val locationModule = module {
    single { PlatformLocationManager() }
}

@OptIn(ExperimentalForeignApi::class)
actual class PlatformLocationManager {

    private var continuation: CancellableContinuation<LocationData?>? = null

    private val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {

        override fun locationManager(
            manager: CLLocationManager,
            didUpdateLocations: List<*>
        ) {
            val location = didUpdateLocations.lastOrNull() as? CLLocation ?: return
            val locationData = location.coordinate.useContents {
                LocationData(
                    latitude = latitude,
                    longitude = longitude
                )
            }

            Napier.d {
                "PlatformLocationManager: lat=${locationData.latitude}, lon=${locationData.longitude}"
            }

            continuation?.resume(locationData)

            continuation = null

            manager.stopUpdatingLocation()
        }

        override fun locationManager(
            manager: CLLocationManager,
            didFailWithError: NSError
        ) {
            Napier.e { "PlatformLocationManager: failed, code=${didFailWithError.code}" }

            continuation?.resume(null)
            continuation = null

            manager.stopUpdatingLocation()
        }

        override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {
            if (continuation == null) return

            when (manager.authorizationStatus) {
                kCLAuthorizationStatusAuthorizedAlways,
                kCLAuthorizationStatusAuthorizedWhenInUse -> manager.startUpdatingLocation()

                kCLAuthorizationStatusNotDetermined -> manager.requestWhenInUseAuthorization()

                kCLAuthorizationStatusDenied,
                kCLAuthorizationStatusRestricted -> {
                    Napier.e { "PlatformLocationManager: authorization denied" }
                    continuation?.resume(null)
                    continuation = null
                    manager.stopUpdatingLocation()
                }

                else -> {
                    Napier.e { "PlatformLocationManager: unexpected authorization status" }
                    continuation?.resume(null)
                    continuation = null
                    manager.stopUpdatingLocation()
                }
            }
        }
    }

    actual suspend fun getCurrentLocation(): LocationData? =
        withTimeoutOrNull(TIMEOUT_MS.milliseconds) {
            withContext(Dispatchers.Main) {
                suspendCancellableCoroutine { cont ->
                    val manager = CLLocationManager()
                    manager.delegate = delegate
                    manager.desiredAccuracy = kCLLocationAccuracyHundredMeters

                    continuation = cont

                    cont.invokeOnCancellation {
                        manager.stopUpdatingLocation()
                        manager.delegate = null
                        continuation = null
                    }

                    when (manager.authorizationStatus) {
                        kCLAuthorizationStatusAuthorizedAlways,
                        kCLAuthorizationStatusAuthorizedWhenInUse -> manager.startUpdatingLocation()

                        kCLAuthorizationStatusNotDetermined -> manager.requestWhenInUseAuthorization()

                        else -> {
                            Napier.e { "PlatformLocationManager: authorization denied" }
                            cont.resume(null)
                            continuation = null
                        }
                    }
                }
            }
        }

    private companion object {
        const val TIMEOUT_MS = 15_000L
    }
}
