package dev.sumanth.paysquareroot.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.sumanth.paysquareroot.anniversaries
import dev.sumanth.paysquareroot.birthdays

@Composable
fun EventsScreen() {


    Column(modifier = Modifier.fillMaxSize().padding(20.dp, 0.dp, 20.dp, 0.dp).verticalScroll(rememberScrollState())) {

        Box(modifier = Modifier.height(30.dp)) {  }

        Text("Birthdays")
        for (index in 0 until birthdays.length()) {
            val event = birthdays.getJSONObject(index)
            Card(modifier = Modifier.fillMaxWidth().padding(0.dp, 10.dp, 0.dp, 0.dp)) {
                Text(event.getString("shortName"), modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 5.dp))
                Text(event.getString("departmentDescription"), modifier = Modifier.padding(10.dp, 5.dp))
                Text(formattedDate(event.getString("dateOfBirth")), modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 10.dp))
            }
        }

        Box(modifier = Modifier.height(30.dp)) {  }

        Text("Anniversaries")
        for (index in 0 until anniversaries.length()) {
            val event = anniversaries.getJSONObject(index)
            Card(modifier = Modifier.fillMaxWidth().padding(0.dp, 10.dp, 0.dp, 0.dp)) {
                Text(event.getString("shortName"), modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 5.dp))
                Text(event.getString("departmentDescription"), modifier = Modifier.padding(10.dp, 5.dp))
                Text(formattedDate(event.getString("joiningDate")), modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 10.dp))
            }
        }

        Box(modifier = Modifier.height(30.dp)) {  }

    }

}