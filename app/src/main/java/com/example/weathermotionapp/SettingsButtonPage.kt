package com.example.weathermotionapp

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.weathermotionapp.ui.theme.CustomColor
import com.example.weathermotionapp.ui.theme.WeatherMotionAppTheme

@Composable
fun SettingsButtonPage(navController: NavController) {
    val context = LocalContext.current
    val lightLevel = remember { mutableStateOf("Detecting actual light") }
    var isDarkMode by remember { mutableStateOf(false) } //mutable to detect if the app should be on light or dark mode

    DisposableEffect(context) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) //device light sensor
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    if (it.sensor.type == Sensor.TYPE_LIGHT) { //sensor check
                        val lux = it.values[0] //retrieve the light level in lux
                        lightLevel.value = "Ambient Light: $lux lux"
                        isDarkMode = lux < 50 //switch to dark mode if the light level is under 50
                    }
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        lightSensor?.let {
            sensorManager.registerListener(listener, it, SensorManager.SENSOR_DELAY_NORMAL)
        }//register the light sensor listener to start receiving updates

        onDispose {
            sensorManager.unregisterListener(listener)
        }//dispose sensor to prevent memory leaks or unnecessary usage
    }
    WeatherMotionAppTheme(darkTheme = isDarkMode) { //enable light sensor in all the actual screen
        Column {
            Scaffold(
                bottomBar = {
                    BottomAppBar(
                        containerColor = CustomColor,
                    ) {
                        IconButton(onClick = { navController.navigate("home") }) {
                            Icon(Icons.Filled.Home, contentDescription = "Home", tint = Color.White)
                        }

                        IconButton(onClick = { navController.navigate("settings") }) {
                            Icon(
                                Icons.Filled.Settings,
                                contentDescription = "Settings",
                                tint = Color.White
                            )
                        }
                    }
                }
                //Same as before simple bottom bar with two icon for navigation between settings screen and main screen
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(30.dp))
                    Row {
                        Button(
                            onClick = {
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, "Check out the WeatherMotion app !")
                                }
                                context.startActivity(
                                    Intent.createChooser(
                                        shareIntent,
                                        "Share via"
                                    )
                                )
                            },
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(text = "Share the app with other")
                        }
                    }
                    //Intent to share the app
                    Spacer(modifier = Modifier.height(30.dp))
                    Button(onClick = {
                        val settingsIntent = Intent(android.provider.Settings.ACTION_WIFI_SETTINGS)
                        context.startActivity(settingsIntent)
                    }) {
                        Text("Open Wi-Fi Settings")
                    }
                    //Intent to open the wifi page to ensure you are connected
                }
            }
        }
    }
}