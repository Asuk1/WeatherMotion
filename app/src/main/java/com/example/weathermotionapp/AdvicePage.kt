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
            override fun onSensorChanged(event: SensorEvent ?) {
                event ?.let {
                    if (it.sensor.type == Sensor.TYPE_LIGHT) { //sensor check
                        val lux = it.values[0] //retrieve the light level in lux
                        lightLevel.value = "Ambient Light: $lux lux"
                        isDarkMode = lux < 50 //switch to dark mode if the light level is under 50
                    }
                }
            }
            override fun onAccuracyChanged(sensor: Sensor ?, accuracy: Int) {}
        }

        lightSensor ?.let {
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
                    "Clear skies ahead ! It's like the universe gave you the green light for outdoor adventures. Enjoy the sunshine, and maybe even get a selfie with that big blue sky.", //code 0 - Clear sky
                    "Mostly clear ! A few clouds trying to photobomb your perfect day, but don’t worry, they’re not staying long. Time to go for a walk or just chill outside.", //code 1 - Mainly clear
                    "Partly cloudy ! The clouds are just hanging out up there, but don’t let them ruin your good vibes. It’s still a great day to get out and enjoy the fresh air.", //code 2 - Partly cloudy
                    "Overcast skies ! Looks like the sun decided to take a nap. But hey, it's perfect weather to stay cozy or grab a book and enjoy some indoorsy vibes.", //code 3 - Overcast
                    "Foggy morning ahead ! It’s like a mystery novel outside where’s the coffee shop ? Don’t worry though, you’ll find it. Just take it slow and steady.", //code 45 - Fog
                    "Rime fog is here, turning the world into a frosty fairy tale ! Make sure your scarf is extra cozy and take a deep breath watch that mist sparkle in the sunlight.", //code 48 - Depositing rime fog
                    "A light drizzle’s falling ! It’s like nature's way of giving us a gentle shower. Not enough to ruin the day, but don’t forget your umbrella ! It’s just a little drizzle, but it’ll keep you cool.", //code 51 - Light drizzle
                    "Moderate drizzle’s happening ! It’s raining, but it’s that peaceful kind of rain. You can still enjoy the outdoors, but maybe pack a light jacket ... just in case.", //code 53 - Moderate drizzle
                    "Heavy drizzle’s here ! It’s like someone turned on the shower outside. Still, it’s not a full downpour, so maybe rock that waterproof jacket and splash through puddles.", //code 55 - Heavy drizzle
                    "It’s light rain today ! The perfect excuse to wear your cute rain boots and play in the puddles. Don’t forget the umbrella ! You’re going to need it more than you think.", //code 61 - Light rain
                    "Moderate rain is in the forecast ! It’s like the sky is giving us a thorough rinse. Be sure to grab an umbrella and watch out for splashing cars !", //code 63 - Moderate rain
                    "Heavy rain alert ! The sky’s not holding back. Stay dry with a sturdy umbrella, or just enjoy the sound of rain pattering against your window while you relax indoors.", //code 65 - Heavy rain
                    "Light freezing rain today ! That thin layer of ice on the ground might try to make things slippery. Walk with caution, and don’t forget your warm gloves !", //code 66 - Light freezing rain
                    "Heavy freezing rain ? Brace yourself for an icy wonderland ! Ice might be forming everywhere, so it’s a good idea to stay inside or wear some sturdy shoes if you venture out.", //code 67 - Heavy freezing rain
                    "Light snow falling ! Time to catch those snowflakes on your tongue. It’s just enough to make things look magical, so maybe go outside for a quick snowball fight.", //code 71 - Light snow
                    "Moderate snow is falling ! The world’s becoming a winter wonderland. Maybe take a moment to make a snow angel or enjoy a warm drink while watching the snow swirl outside.", //code 73 - Moderate snow
                    "Heavy snow incoming ! Looks like you’re going to need a snow shovel. But don’t worry it’s perfect snowman-building weather. Just stay warm and enjoy the beauty.", //code 75 - Heavy snow
                    "Snow grains are falling ! Not quite snow, not quite hail, but still adding a little winter magic to the day. Time for a cozy blanket and a hot drink.", //code 77 - Snow grains
                    "Light shower rain today ! It’s like the sky can’t make up its mind. Perfect weather for a walk with a light jacket and a bouncy step.", //code 80 - Light shower rain
                    "Moderate shower rain nature’s trying to freshen things up ! Bring out your umbrella, or if you’re feeling adventurous, let the rain cool you down a bit.", //code 81 - Moderate shower rain
                    "Heavy shower rain ! The heavens are pouring today. Grab your umbrella and try not to get soaked... unless you’re into dancing in the rain, in which case, go for it !", //code 82 - Heavy shower rain
                    "Light snow showers ! Snowflakes are dancing in the air. It’s the perfect time to go for a snowy stroll and take in the wintery sights before they melt.", //code 85 - Light snow showers
                    "Heavy snow showers today ! The snowflakes are falling fast and furious. It’s time for some snowball fights and maybe a snow fort—just don’t forget to dress warmly.", //code 86 - Heavy snow showers
                    "Thunder’s rumbling today ! The sky is throwing a tantrum. Time to enjoy the show from inside with some popcorn. You won’t want to miss it !", //code 95 - Thunderstorm
                    "Thunderstorm with light hail ! It’s like nature’s taking it up a notch with thunder and a little icy sprinkle. Stay inside if you can, and if you have to go out, protect your head !", //code 96 - Thunderstorm with light hail
                    "Thunderstorm with heavy hail ! Hail’s coming down hard. Get inside, stay dry, and if you can, watch the dramatic sky show from the safety of your window !" //code 99 - Thunderstorm with heavy hail
                )
                //list of advice text based on some weather. I tried to make some inner joke to make it entertain and funny for the user
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
