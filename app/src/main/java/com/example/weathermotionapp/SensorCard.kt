package com.example.weathermotionapp

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.weathermotionapp.ui.theme.CustomColor

@Composable
fun SensorInfoCard(navController: NavController) {
    val context = LocalContext.current
    val accelerometerData = remember { mutableStateOf("Loading actual accelerometer data") } //mutable to store accelerometer data
    val gyroscopeData = remember { mutableStateOf("Loading actual gyroscope data") } //mutable for gyro data

    DisposableEffect(context) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        val listener = object : SensorEventListener { //listener to handle sensor data changes
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    when (it.sensor.type) {
                        Sensor.TYPE_ACCELEROMETER -> {
                            val x = it.values[0]
                            val y = it.values[1]
                            val z = it.values[2] //x,y,z value from sensor accelerometer
                            accelerometerData.value = "Accelerometer: x=$x, y=$y, z=$z"
                        }
                        Sensor.TYPE_GYROSCOPE -> {
                            val x = it.values[0]
                            val y = it.values[1]
                            val z = it.values[2]//x,y,z value from sensor gyro
                            gyroscopeData.value = "Gyroscope: x=$x, y=$y, z=$z"
                            if (Math.abs(x) > 2|| Math.abs(y) > 2 || Math.abs(z) > 2) {
                                // Trigger navigation to settings on rotation
                                navController.navigate("settings")
                            }
                        }
                    }
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        accelerometer?.let {
            sensorManager.registerListener(listener, it, SensorManager.SENSOR_DELAY_NORMAL)
        }//register the accelerometer sensor listener to start receiving updates
        gyroscope?.let {
            sensorManager.registerListener(listener, it, SensorManager.SENSOR_DELAY_NORMAL)
        }//register the gyro sensor listener to start receiving updates

        onDispose {
            sensorManager.unregisterListener(listener)
        }//dispose sensor to prevent memory leaks or unnecessary usage
    }
    Card(
        border = BorderStroke(1.dp, CustomColor),
        modifier = Modifier
            .size(width = 300.dp, height = 250.dp)
            .padding(20.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(text = accelerometerData.value) //display accelerometer value
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = gyroscopeData.value)//display gyro value
        }
    }
}