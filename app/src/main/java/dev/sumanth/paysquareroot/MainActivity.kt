package dev.sumanth.paysquareroot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.sumanth.paysquareroot.ui.component.Background
import dev.sumanth.paysquareroot.ui.component.TopBar
import dev.sumanth.paysquareroot.ui.screen.HomeScreen
import dev.sumanth.paysquareroot.ui.screen.LoginScreen
import org.json.JSONObject
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

var dahboardData: JSONObject = JSONObject()
var lmsData: JSONObject = JSONObject()
var punchTimes: JSONObject = JSONObject()

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            var topBarText by remember { mutableStateOf("Login") }
            Background {
                NavHost(navController = navController, startDestination = "Login") {
                    composable("Login") {
                        topBarText = "Login"
                        LoginScreen(navController)
                    }
                    composable("Home") {
                        topBarText = "Dashboard"
                        HomeScreen()
                    }
                }
            }
        }

    }

}