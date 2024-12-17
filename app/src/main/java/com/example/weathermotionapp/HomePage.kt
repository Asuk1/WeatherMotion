package com.example.weathermotionapp

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.http.HttpResponseCache.install
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.weathermotionapp.SensorInfoCard
import com.example.weathermotionapp.ui.theme.CustomColor
import com.example.weathermotionapp.ui.theme.WeatherMotionAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavController) {
    val context = LocalContext.current
    var countryMenuExpanded by remember { mutableStateOf(false) }  //country dropdown
    var moreOptionsMenuExpanded by remember { mutableStateOf(false) } //more options dropdown (could have named advice options instead but in principle if I want to add more option later)
    val lightLevel = remember { mutableStateOf("Detecting actual light ") }
    var isDarkMode by remember { mutableStateOf(false) } //mutable to detect if the app should be on light or dark mode

    var isLoading by remember { mutableStateOf(true) } //state variable to track whether data is currently being loaded
    var weatherData by remember { mutableStateOf<WeatherResponse?>(null) }

    var selectedCountry by remember { mutableStateOf<Country?>(null) }
    var countryName by remember { mutableStateOf("Select a country") }
    val interactionSource = remember { MutableInteractionSource() } //state object to handle user interaction events

    DisposableEffect(context) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) //device light sensor
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    if (it.sensor.type == Sensor.TYPE_LIGHT) { //sensor check
                        val lux = it.values[0] //retrieve the light level in lux
                        lightLevel.value = "Ambient Light: $lux lux"
                        isDarkMode = lux < 60 //switch to dark mode if the light level is under 50
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

    LaunchedEffect(selectedCountry) {
        selectedCountry?.let {
            isLoading = true
            weatherData = fetchWeather(it.latitude, it.longitude) //fetch weather data for the selected country's latitude and longitude
            isLoading = false
        }
    } //launch the weather api data when country selected (it is like a useeffect in react)

    WeatherMotionAppTheme(darkTheme = isDarkMode) { //enable sensor is all the actual screen
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Home Page", color = Color.White) },
                    colors = topAppBarColors(
                        containerColor = CustomColor,
                    ),
                    actions = {
                        IconButton(onClick = { moreOptionsMenuExpanded = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More Options",
                                tint = Color.White
                            )
                        }
                        DropdownMenu(
                            expanded = moreOptionsMenuExpanded,
                            onDismissRequest = {
                                moreOptionsMenuExpanded = false
                            }
                        ) {
                            DropdownMenuItem(
                                onClick = {
                                    moreOptionsMenuExpanded = false
                                    navController.navigate("advice")
                                },
                                text = {
                                    Text("Advice")
                                }
                            )
                        }
                    }) //dropdown menu for advice
            },
            //top bar with a title and menu on the right
            bottomBar = {
                BottomAppBar(
                    containerColor = CustomColor,
                ) {
                    IconButton(onClick = {}) {
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
            //Simple bottom bar with two icon for navigation between settings screen and main screen
        ) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                Text(
                    text = "Selected Country: $countryName by clicking here",
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            countryMenuExpanded = !countryMenuExpanded //toggle the dropdown
                        }
                )

                DropdownMenu(
                    expanded = countryMenuExpanded,
                    onDismissRequest = { countryMenuExpanded = false }
                ) {
                    countries.forEach { country ->
                        DropdownMenuItem(
                            text = { Text(text = country.name) },
                            onClick = {
                                countryMenuExpanded= false
                                selectedCountry = country //update the selectedCountry state
                                countryName = country.name //updated the displayed Country name
                            },
                            interactionSource = interactionSource
                        )
                    }
                }
                //dropdown menu for the country list
                Card(
                    border = BorderStroke(1.dp, Color.Blue),
                    modifier = Modifier
                        .size(width = 300.dp, height = 175.dp)
                        .padding(20.dp)
                ) { if (isLoading) {
                    Text(text = "Loading weather data...", modifier = Modifier.padding(20.dp))
                } else {
                    weatherData?.let { data ->
                        val currentWeatherCode = data.hourly.weather_code.firstOrNull() ?: 0
                        val weatherDescription = getWeatherDescription(currentWeatherCode)
                        Text(
                            text = "Temperature: ${data.hourly.temperature_2m.firstOrNull()}°C",
                            modifier = Modifier.padding(20.dp)
                        ) //display temperature depending the country selected in the dropdown menu
                        Text(
                            text = "Weather: $weatherDescription",
                            modifier = Modifier.padding(20.dp)
                        )//display weather depending the country selected in the dropdown menu
                    } ?: run {
                        Text(
                            text = "Failed to fetch weather data.",
                            modifier = Modifier.padding(20.dp)
                        )
                    }
                    }
                }//card for weather api
                SensorInfoCard(navController = navController)
                //The card that host device actual information for sensor
            }
        }
    }
}

