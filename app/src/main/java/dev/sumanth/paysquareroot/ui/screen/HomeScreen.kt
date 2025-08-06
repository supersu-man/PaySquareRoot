package dev.sumanth.paysquareroot.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.sumanth.paysquareroot.dahboardData
import dev.sumanth.paysquareroot.lmsData
import dev.sumanth.paysquareroot.punchTimes

@Preview(showBackground = true)
@Composable
fun HomeScreen() {

    Column(modifier = Modifier.fillMaxSize().padding(10.dp).verticalScroll(rememberScrollState())) {

        PunchTimes()
        LeaveBalance()
        BirthDays()
        Anniversaries()
    }
}


@Composable
fun LeaveBalance() {
    val leaveBalance = lmsData.getJSONArray("leaveBalance")
    Card(modifier = Modifier.fillMaxWidth().padding(0.dp, 0.dp, 0.dp, 10.dp)) {
        Text("Leave Balance", modifier = Modifier.padding(10.dp), fontSize = 28.sp)
        Column (modifier = Modifier.padding(10.dp)) {
            Row(modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 10.dp)) {
                Text("Leave type", modifier = Modifier.weight(0.5f))
                Text("Opening", modifier = Modifier.weight(0.25f))
                Text("Closing", modifier = Modifier.weight(0.25f))
            }
            for (i in 0 until leaveBalance.length()){
                val leavebal = leaveBalance.getJSONObject(i)
                Row {
                    Text(leavebal.getString("leaveDescription"), modifier = Modifier.weight(0.5f))
                    Text(leavebal.getString("leaveOpeningBalance"), modifier = Modifier.weight(0.25f))
                    Text(leavebal.getString("balanceLeave"), modifier = Modifier.weight(0.25f))
                }
            }
        }
    }

}

@Composable
fun BirthDays() {
    val birthdays = dahboardData.getJSONArray("birthday")
    Card(modifier = Modifier.fillMaxWidth().padding(0.dp, 0.dp, 0.dp, 10.dp)) {
        Text("Birthdays", modifier = Modifier.padding(10.dp), fontSize = 28.sp)
        Column (modifier = Modifier.padding(10.dp)) {
            for (i in 0 until birthdays.length()){
                val birthday = birthdays.getJSONObject(i)
                Text(birthday.getString("shortName"))
                Text("Birthdate: " + birthday.getString("dateOfBirth"))
                Text("Department: " + birthday.getString("departmentDescription"), modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 10.dp))
            }
        }
    }
}

@Composable
fun Anniversaries() {
    val anniversaries = dahboardData.getJSONArray("anniversary")
    Card(modifier = Modifier.fillMaxWidth()) {
        Text("Anniversaries", modifier = Modifier.padding(10.dp), fontSize = 28.sp)
        Column(modifier = Modifier.padding(10.dp)) {
            for (i in 0 until anniversaries.length()){
                val birthday = anniversaries.getJSONObject(i)
                Text(birthday.getString("shortName"))
                Text("Joining date: " + birthday.getString("joiningDate"))
                Text("Department: " + birthday.getString("departmentDescription"), modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 10.dp))
            }
        }
    }
}


@Composable
fun PunchTimes() {
    val punchTimes = punchTimes.getJSONArray("dtAttendance")
    println(punchTimes)
    Card(modifier = Modifier.fillMaxWidth().padding(0.dp, 0.dp, 0.dp, 10.dp)) {
        Text("Punch Times", modifier = Modifier.padding(10.dp), fontSize = 28.sp)
        Column (modifier = Modifier.padding(10.dp)) {
            for (i in 0 until punchTimes.length()){
                val punchTime = punchTimes.getJSONObject(i)
                Row {
                    Text(punchTime.getString("punch_time"))
                }
            }
        }
    }
}
