package com.example.weathermotionapp

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class WeatherResponse(
    val hourly: Hourly
)

@Serializable
data class Hourly(
    val temperature_2m: List<Double>,
    val weather_code: List<Int>
)
//data from the api url that I want

fun getWeatherDescription(weatherCode: Int): String {
    return when (weatherCode) {
        0 -> "Clear sky"
        1 -> "Mainly clear"
        2 -> "Partly cloudy"
        3 -> "Overcast"
        45 -> "Fog"
        48 -> "Depositing rime fog"
        51 -> "Light drizzle"
        53 -> "Moderate drizzle"
        55 -> "Heavy drizzle"
        61 -> "Light rain"
        63 -> "Moderate rain"
        65 -> "Heavy rain"
        66 -> "Light freezing rain"
        67 -> "Heavy freezing rain"
        71 -> "Light snow"
        73 -> "Moderate snow"
        75 -> "Heavy snow"
        77 -> "Snow grains"
        80 -> "Light shower rain"
        81 -> "Moderate shower rain"
        82 -> "Heavy shower rain"
        85 -> "Light snow showers"
        86 -> "Heavy snow showers"
        95 -> "Thunderstorm"
        96 -> "Thunderstorm with light hail"
        99 -> "Thunderstorm with heavy hail"
        else -> "Unknown weather"
    }
}
//weather code in open meteo's doc

suspend fun fetchWeather(latitude: Double, longitude: Double): WeatherResponse? {
    val client = HttpClient(CIO)

    return try {
        val response = client.get("https://api.open-meteo.com/v1/forecast") {
            parameter("latitude", latitude)
            parameter("longitude", longitude)
            parameter("hourly", "temperature_2m,weather_code")
        } //I selected these two parameter because it is what I search for although open meteo has so much parameter interesting for data research
        if (response.status.isSuccess()) {
            val responseBody = response.bodyAsText()

            //manually parsing the JSON response string into WeatherResponse because import io.ktor.client.plugins.contentnegotiation.json is unresolved
            Json { ignoreUnknownKeys = true }.decodeFromString<WeatherResponse>(responseBody)
        } else {
            println("Error fetching weather data: ${response.status}")
            null
        }
    } catch (e: Exception) {
        println("Error occurred: ${e.localizedMessage}")
        e.printStackTrace()
        null
    } finally {
        client.close()
    }
}
//fetch/suspend weather api and parameter function

@Serializable
data class Country(val name: String, val latitude: Double, val longitude: Double)

val countries = listOf(
    Country("USA", 37.7749, -122.4194),
    Country("Germany", 52.52, 13.41),
    Country("Japan", 35.6895, 139.6917),
    Country("India", 28.6139, 77.2090),
    Country("France", 48.8566, 2.3522),
    Country("Brazil", -23.5505, -46.6333),
    Country("UK", 51.5074, -0.1278),
    Country("Canada", 45.4215, -75.6992),
    Country("Australia", -33.8688, 151.2093),
    Country("Russia", 55.7558, 37.6173),
    Country("Mexico", 19.4326, -99.1332)
)
//list of important country