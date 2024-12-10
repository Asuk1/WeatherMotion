package com.example.weathermotionapp

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.weathermotionapp.ui.theme.CustomColor
import com.example.weathermotionapp.ui.theme.WeatherMotionAppTheme


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AdvicePage(navController: NavController) {
    val context = LocalContext.current
    val lightLevel = remember { mutableStateOf("Detecting light acutal light") }
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

    WeatherMotionAppTheme(darkTheme = isDarkMode) {
        Scaffold(
            bottomBar = {
                BottomAppBar(
                    containerColor = CustomColor,
                ) {
                    IconButton(onClick = {navController.navigate("home")}) {
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
            }//Simple bottom bar with two icon for navigation between settings screen and main screen
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {//use lazycolumn so the screen can scroll
                // weather advice items
                val adviceList = listOf(
                    "Enjoy the sunny day! Don't forget your sunglasses and wear something light and comfortable.",
                    "It’s rainy! Be careful on the roads and carry an umbrella. You don't want to get soaked!",
                    "It’s stormy out there! The wind is on a mission to blow your hat away, so hang tight and maybe stay indoors unless you're into spontaneous hairdos.",
                    "The wind is strong today! It’s like nature’s way of telling you to hold onto your hat. Don’t let it fly away!",
                    "The weather is as confused as a cat on a leash today! One minute it’s sunny, the next minute it’s raining. Don’t forget your umbrella, and maybe pack a sweater too!",
                    "Foggy morning ahead! The world is shrouded in mystery. Don’t worry though, the fog will clear up eventually, and you’ll probably find your way to your coffee in time.",
                    "Snowy days are perfect for building snowmen and pretending you’re in a winter wonderland. Just make sure you’re wearing more than one pair of socks—trust me, your toes will thank you.",
                    "It’s a scorcher today! The sun is trying to cook us all. Stay hydrated and remember, don’t touch anything metal outside. It’s not a nice surprise!",
                    "It’s chilly! Time to channel your inner penguin and snuggle up with a warm blanket. If you’re outside, bring your personal heater (aka your coat).",
                    "Thunder’s rumbling today! The sky is throwing a tantrum. Time to enjoy the show from inside with some popcorn. You won’t want to miss it!"
                ) //list of advice text based on some weather. I tried to make some inner joke to make it entertain and funny for the user
                // using itemsIndexed to iterate over each item
                itemsIndexed(adviceList) { index, adviceText ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .background(
                                color = if (isDarkMode) Color.DarkGray else Color.White //change background based on my light sensor ( basically if it is dark mode or light mode)
                            )
                            .border(
                                width = 2.dp,
                                color = CustomColor,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(16.dp)
                    ) {
                        Text(
                            text = adviceText,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(horizontal = 20.dp),
                            color = CustomColor,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic,
                            lineHeight = 24.sp,
                            letterSpacing = 1.5.sp
                        )
                    }
                    //add space after each item except for the last one
                    if (index != adviceList.lastIndex) {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}
