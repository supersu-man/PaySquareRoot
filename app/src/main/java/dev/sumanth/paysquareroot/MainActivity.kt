package dev.sumanth.paysquareroot

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.sumanth.paysquareroot.ui.component.Background
import dev.sumanth.paysquareroot.ui.component.UpdateDialog
import dev.sumanth.paysquareroot.ui.screen.HomeScreen
import dev.sumanth.paysquareroot.ui.screen.LoginScreen
import dev.sumanth.paysquareroot.ui.screen.showToast
import dev.sumanth.paysquareroot.utils.getChangelogJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import androidx.core.net.toUri


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
            var showPopup by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) {
                checkAppUpdate {
                    showPopup = true
                }
            }
            Background {
                NavHost(navController = navController, startDestination = "Login") {
                    composable("Login") {
                        LoginScreen(navController)
                    }
                    composable("Home") {
                        HomeScreen()
                    }
                }
                if(showPopup) {
                    UpdateDialog(onDismiss = { showPopup = false }, onConfirm = {
                        val browserIntent = Intent(Intent.ACTION_VIEW, "https://sumanth.dev/paysquareroot".toUri())
                        startActivity(browserIntent)
                    })
                }
            }
        }

    }

    fun checkAppUpdate(callback: () -> Unit) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val changelogJson = getChangelogJson()
            val lastObject = changelogJson.getJSONObject(changelogJson.length()-1)
            val versionCode = BuildConfig.VERSION_CODE
            val latestVersion = lastObject.getInt("versionCode")
            if(latestVersion > versionCode) {
                callback()
            }
        } catch (e: Exception) {
            showToast(this@MainActivity, "Failed to check app updates")
        }
    }

}