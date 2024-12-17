package com.example.weathermotionapp

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.ExtendedFloatingActionButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.weathermotionapp.ui.theme.CustomColor
import com.example.weathermotionapp.ui.theme.WeatherMotionAppTheme

@Composable
fun SettingPage(navController: NavController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)//access to shared preference data
    val savedName = sharedPreferences.getString("user_name", "No name found") ?: "No name found" //the data/name found in shared preference that is the one the user wrote to log in
    val lightLevel = remember { mutableStateOf("Detecting actual light") }
    var isDarkMode by remember { mutableStateOf(false) } //mutable to detect if the app should be on light or dark mode
    val accelerometerData = remember { mutableStateOf("Loading actual accelerometer data") } //mutable to store accelerometer data

    DisposableEffect(context) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) //device light sensor
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) //device accelerometer sensor
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    when (it.sensor.type) {
                        Sensor.TYPE_LIGHT -> {
                            // Retrieve the ambient light level in lux.
                            val lux = it.values[0]
                            lightLevel.value = "Ambient Light: $lux lux"
                            isDarkMode = lux < 60 // Enable dark mode if the light level is low.
                        }
                        Sensor.TYPE_ACCELEROMETER -> {
                            // Retrieve accelerometer values for x, y, and z axes.
                            val x = it.values[0]
                            val y = it.values[1]
                            val z = it.values[2]
                            accelerometerData.value = "Accelerometer: x=$x, y=$y, z=$z"
                            if (Math.abs(x) > 8 || Math.abs(y) > 8) {
                                navController.navigate("home") // Redirect to home page with rotation
                            }
                        }
                    }
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        lightSensor?.let {
            sensorManager.registerListener(listener, it, SensorManager.SENSOR_DELAY_NORMAL)
        }//register the light sensor listener to start receiving updates

        accelerometer?.let {
            sensorManager.registerListener(listener, it, SensorManager.SENSOR_DELAY_NORMAL)
        }//register the accelerometer sensor listener to start receiving updates

        onDispose {
            sensorManager.unregisterListener(listener)
        }//dispose sensor to prevent memory leaks or unnecessary usage
    }
    WeatherMotionAppTheme(darkTheme = isDarkMode) { //enable the sensor is all the actual screen
        Column {
            Scaffold(
                bottomBar = {
                    BottomAppBar(
                        containerColor = CustomColor,
                    ) {
                        IconButton(onClick = { navController.navigate("home") }) {
                            Icon(Icons.Filled.Home, contentDescription = "Home", tint = Color.White)
                        }

                        IconButton(onClick = {}) {
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
                    Icon(
                        Icons.Filled.AccountCircle, "Person name", modifier = Modifier
                            .size(150.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(50.dp))
                    Row {
                        Card {
                            Icon(
                                Icons.Filled.Person, "Person name", modifier = Modifier
                                    .size(50.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                            Text(text = "$savedName", textAlign = TextAlign.Center, modifier = Modifier.padding(10.dp)) //display the name used to log in by retrieving the data saved into shared preference
                        }
                    }
                    // Card that will host the name you put when log in for next milestone
                    Spacer(modifier = Modifier.height(30.dp))
                    Row {
                        ExtendedFloatingActionButton(onClick = { navController.navigate("settings button") }) {
                            Icon(
                                Icons.Filled.Build,
                                "Lighting mode",
                                modifier = Modifier
                                    .size(50.dp)
                                    .align(Alignment.CenterVertically)
                            )
                            Text(text = "Settings")
                        }
                    }
                    //Fab that redirect into the settings options and intent
                    Spacer(modifier = Modifier.height(30.dp))
                    Row {
                        ExtendedFloatingActionButton(onClick = { navController.navigate("login") }) {
                            Icon(
                                Icons.Filled.ExitToApp,
                                "Logout",
                                modifier = Modifier
                                    .size(50.dp)
                                    .align(Alignment.CenterVertically)
                            )
                            Text(text = "Exit")
                        }
                    }
                    //Fab that  redirect you to the login page and reset the shared preference name to log in
                }
            }
        }
    }
}


