package com.noman.weatherx

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
