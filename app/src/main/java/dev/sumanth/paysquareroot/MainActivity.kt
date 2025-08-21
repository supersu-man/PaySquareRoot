package dev.sumanth.paysquareroot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.sumanth.paysquareroot.ui.component.Background
import dev.sumanth.paysquareroot.ui.screen.HomeScreen
import dev.sumanth.paysquareroot.ui.screen.LoginScreen
import org.json.JSONArray
import org.json.JSONObject

var birthdays = JSONArray()
var anniversaries = JSONArray()
var leaves = JSONArray()
var punchTimes = JSONArray()
var companyData = JSONObject()
var holidayList = JSONArray()

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            Background {
                NavHost(navController = navController, startDestination = "Login") {
                    composable("Login") {
                        LoginScreen(navController)
                    }
                    composable("Home") {
                        HomeScreen()
                    }
                }
            }
        }

    }

}