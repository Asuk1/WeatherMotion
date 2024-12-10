package com.example.weathermotionapp

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
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


@Composable
fun ExplanationPageNext(navController: NavController) {
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
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 20.dp)
                    .background(
                        color = Color.White
                    )
                    .border(
                        width = 2.dp,
                        color = CustomColor,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = "Two things to remember : Roatate or tilt your phone to navigate ! You can also see your device information on these sensors.",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 20.dp),
                    color = CustomColor,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,  // set the text style to italic  and bold for a stylish look
                    lineHeight = 24.sp,
                    letterSpacing = 1.5.sp  // add spacing between letters for a more stylized effect
                )//description of the sensor use of my app to the user
            }//box for the description of my app to make it more attractive and understandable to the user
            Spacer(modifier = Modifier.height(30.dp))
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 20.dp)
                    .background(
                        color = Color.White
                    )
                    .border(
                        width = 2.dp,
                        color = CustomColor,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = "On the menu you can see some advice based on the weather ! It is scrollable by the way !",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 20.dp),
                    color = CustomColor,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,  // set the text style to italic  and bold for a stylish look
                    lineHeight = 24.sp,
                    letterSpacing = 1.5.sp  // add spacing between letters for a more stylized effect
                )//description of what you can do on the app
            }//box for the description of my app to make it more attractive and understandable to the user
            Spacer(modifier = Modifier.height(30.dp))
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 20.dp)
                    .background(
                        color = Color.White
                    )
                    .border(
                        width = 2.dp,
                        color = CustomColor,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = "On the settings screen you can access to more settings to verify your wifi or share the app to your friends to support us !",
                    //well I said support us but i am a single dev, it is to pretend that it is an app build by an active team...
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 20.dp),
                    color = CustomColor,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,  // set the text style to italic  and bold for a stylish look
                    lineHeight = 24.sp,
                    letterSpacing = 1.5.sp  // add spacing between letters for a more stylized effect
                )//description of other resources the user can do on settings
            }//box for the description of my app to make it more attractive and understandable to the user
            Spacer(modifier = Modifier.height(30.dp))
            Button(onClick = {
                navController.navigate("home") }, colors = ButtonDefaults.buttonColors(
                CustomColor
            )) {
                Text(text = "Let's start the app !")
            }
            //Button to click to be able to really access main resource of the app
        }
    }
}

