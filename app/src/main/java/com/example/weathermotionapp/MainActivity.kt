package com.example.weathermotionapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
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

private var yourName = mutableStateOf("Your name")
@Composable
fun LoginPage(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        HorizontalDivider(modifier = Modifier.height(50.dp))
        Text(text = "WeatherMotion", color = Color.Blue, fontSize = 30.sp)
        HorizontalDivider(modifier = Modifier.height(24.dp))
        LogoApp()
        TextField(
            value = yourName.value,
            onValueChange = {yourName.value = it}
        )
        Button(onClick = {navController.navigate("home")}) {
            Text(text = "Log in")
        }
    }
}

@Composable
fun LogoApp() {
    Box {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "main logo weather",
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.Center)
        )
        Image(
            painter = painterResource(id = R.drawable.shake),
            contentDescription = "main logo shake",
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.CenterStart)
        )
    }
}
@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "login") {
        composable("login") { LoginPage(navController)}
        composable("home") { HomePage(navController) }
        composable("settings") { SettingPage(navController) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavController)  {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Home Page") },
                colors = topAppBarColors(
                    containerColor = Color.Cyan
                ),
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.Cyan
            ) {
                IconButton(onClick = {}) {
                    Icon(Icons.Filled.Home, contentDescription = "Home", tint = Color.White)
                }
                IconButton(onClick = { navController.navigate("settings") }) {
                    Icon(Icons.Filled.Settings, contentDescription = "Settings", tint = Color.White)
                }
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Card(
                border = BorderStroke(1.dp, Color.Blue),
                modifier = Modifier
                    .size(width = 300.dp, height = 250.dp)
                    .padding(20.dp)
            ) {
                Text(text = "Card for weather data", modifier = Modifier.padding(20.dp))
            }
            Card(
                border = BorderStroke(1.dp, Color.Blue),
                modifier = Modifier
                    .size(width = 300.dp, height = 250.dp)
                    .padding(20.dp)
            ) {
                Text(text = "Card for position data", modifier = Modifier.padding(20.dp))
            }
        }
    }
}

@Composable
fun SettingPage(navController: NavController) {
    Column {
        Scaffold(
            bottomBar = {
                BottomAppBar(
                    containerColor = Color.Cyan
                ) {
                    IconButton(onClick = {navController.navigate("home")}) {
                        Icon(Icons.Filled.Home, contentDescription = "Home", tint = Color.White)
                    }

                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings", tint = Color.White)
                    }
                }
            }
        ) { paddingValues ->
            Column(modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Icon(Icons.Filled.AccountCircle, "Person name", modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.CenterHorizontally))
                Spacer(modifier = Modifier.height(50.dp))
                Row {
                    Card {
                        Icon(Icons.Filled.Person, "Person name", modifier = Modifier
                            .size(50.dp)
                            .align(Alignment.CenterHorizontally))
                        Text(text = "Your name")
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
                Row {
                    ExtendedFloatingActionButton(onClick = {}) {
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
                Spacer(modifier = Modifier.height(30.dp))
                Row {
                    ExtendedFloatingActionButton(onClick = {navController.navigate("login")}) {
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
            }
        }
    }
}