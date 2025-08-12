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
import org.json.JSONObject

var dahboardData: JSONObject = JSONObject()
var lmsData: JSONObject = JSONObject()
var punchTimes: JSONObject = JSONObject()

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