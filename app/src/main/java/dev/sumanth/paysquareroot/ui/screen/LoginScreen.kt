package dev.sumanth.paysquareroot.ui.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import dev.sumanth.paysquareroot.dahboardData
import dev.sumanth.paysquareroot.lmsData
import dev.sumanth.paysquareroot.punchTimes
import dev.sumanth.paysquareroot.utils.PreferencesManager
import dev.sumanth.paysquareroot.utils.SSOUT
import dev.sumanth.paysquareroot.utils.authenticate
import dev.sumanth.paysquareroot.utils.dummyRequests
import dev.sumanth.paysquareroot.utils.getAccessTokenObject
import dev.sumanth.paysquareroot.utils.getDashboardData
import dev.sumanth.paysquareroot.utils.getData
import dev.sumanth.paysquareroot.utils.getLmsDashboardData
import dev.sumanth.paysquareroot.utils.getParams
import dev.sumanth.paysquareroot.utils.getPunchTimes
import dev.sumanth.paysquareroot.utils.getRedirectUrl
import dev.sumanth.paysquareroot.utils.landingPage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.nodes.Document

@Composable
fun LoginScreen(navController: NavHostController) {
    val context = LocalContext.current
    val preferences = PreferencesManager(context)

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        preferences.getUsername.collect {
            username = it ?: ""
        }
    }

    LaunchedEffect(Unit) {
        preferences.getPassword.collect {
            password = it ?: ""
        }
    }

    var passwordVisibility by remember { mutableStateOf(false) }
    var loader by remember { mutableStateOf(false) }

    var loginPage: Document? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit)  {
        CoroutineScope(Dispatchers.IO).launch {
            val body = landingPage()
            loginPage = body
        }
    }

    fun login(username: String, password: String, preferences: PreferencesManager ) = CoroutineScope(Dispatchers.IO).launch {
        if(loginPage == null) return@launch
        loader = true

        val isSuccess = authenticate(loginPage as Document, username, password)
        if(!isSuccess) {
            loader = false
            showToast(context, "Unable to authenticate")
            return@launch
        }
        preferences.saveCredentials(username, password)
        val redirectUrl = getRedirectUrl()
        val params = getParams(redirectUrl)
        val accessTokenObject = getAccessTokenObject(params["onresuerauqsyap"] as String, false)
        getData(accessTokenObject, params, redirectUrl, false)
        dahboardData = getDashboardData(accessTokenObject)
        val accessTokenObjectLMS = getAccessTokenObject(params["onresuerauqsyap"] as String, true)
        getData(accessTokenObjectLMS, params, redirectUrl, true)
        lmsData = getLmsDashboardData(accessTokenObjectLMS)
        punchTimes = getPunchTimes(accessTokenObjectLMS)
        loader = false
        launch(Dispatchers.Main) {
            navController.navigate("Home")
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Card {
            Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Username") })
                OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") },
                    visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = { IconToggleButton(checked = passwordVisibility, onCheckedChange = { passwordVisibility = it }) {
                        Icon(imageVector = if(passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, contentDescription = "")
                    } }
                )
                Button(modifier = Modifier.padding(0.dp, 20.dp, 0.dp, 0.dp).height(40.dp), onClick = { login(username, password, preferences) }) {
                    if(loader) {
                        CircularProgressIndicator(modifier = Modifier.fillMaxHeight().aspectRatio(1f), color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Text("Login")
                    }
                }
            }
        }
    }

}

fun showToast(context: Context, text: String) = CoroutineScope(Dispatchers.Main).launch {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

