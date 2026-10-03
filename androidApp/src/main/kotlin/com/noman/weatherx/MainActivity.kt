package com.noman.weatherx

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.noman.weatherx.ui.home.HomeScreen
import com.noman.weatherx.ui.theme.WeatherXTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            WeatherXTheme {
                Scaffold { innerPadding ->
                    HomeScreen(contentPadding = innerPadding)
                }
            }
        }
    }
}
