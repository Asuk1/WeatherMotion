package com.example.weathermotionapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weathermotionapp.ui.theme.WeatherMotionAppTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherMotionAppTheme {
                AppNavHost()
            }
        }
    }
}


@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "login") {
        composable("login") { LoginPage(navController)}
        composable("explanation") { ExplanationPage(navController) }
        composable("next") { ExplanationPageNext(navController) }
        composable("home") { HomePage(navController) }
        composable("advice") { AdvicePage(navController) }
        composable("settings") { SettingPage(navController) }
        composable("settings button") { SettingsButtonPage(navController)}
    }
}
//The function that controls the navigation between my screen






