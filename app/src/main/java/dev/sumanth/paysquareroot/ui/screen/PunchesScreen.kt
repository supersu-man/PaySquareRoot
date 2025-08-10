package dev.sumanth.paysquareroot.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.sumanth.paysquareroot.punchTimes

@Composable
fun PunchesScreen() {
    val punchTimes = punchTimes.getJSONArray("dtAttendance")
    val firstPunchTime = getPunchInTime(punchTimes)
    val lastPunchTime = getLastPunchTime(punchTimes)

    Column(modifier = Modifier.fillMaxSize().padding(10.dp).verticalScroll(rememberScrollState())) {

//        Text("Today", modifier = Modifier.padding(5.dp, 20.dp, 0.dp, 10.dp))
        Card(modifier = Modifier.fillMaxWidth().padding(5.dp)) {
            Text("Punch In", modifier = Modifier.padding(10.dp, 10.dp))
            Text(firstPunchTime, modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 10.dp))
        }
        Card(modifier = Modifier.fillMaxWidth().padding(5.dp)) {
            Text("Punch Out", modifier = Modifier.padding(10.dp, 10.dp))
            Text(lastPunchTime, modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 10.dp))
        }

        Text("All punches", modifier = Modifier.padding(5.dp, 20.dp, 0.dp, 10.dp))
        for (i in 0 until punchTimes.length()) {
            val obj = punchTimes.getJSONObject(i)
            val punchDate = obj.getString("punch_time") as String
            val punchTime = punchDate.split(" ")[1]

            Card(modifier = Modifier.weight(0.5f).padding(5.dp)) {
                Text("Punch In", modifier = Modifier.padding(10.dp, 10.dp))
                Text(punchTime, modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 10.dp))
            }
        }

    }

}