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
import dev.sumanth.paysquareroot.dahboardData
import dev.sumanth.paysquareroot.lmsData
import dev.sumanth.paysquareroot.punchTimes
import org.json.JSONArray
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Preview(showBackground = true)
@Composable
fun DashboardScreen() {
    val punchTimes = punchTimes.getJSONArray("dtAttendance")
    val leaveBalance = lmsData.getJSONArray("leaveBalance")
    val birthdays = dahboardData.getJSONArray("birthday")
    val anniversaries = dahboardData.getJSONArray("anniversary")

    val firstPunchTime = getPunchInTime(punchTimes)
    val lastPunchTime = getLastPunchTime(punchTimes)
    val balanceLeaves = getLeaveBalance(leaveBalance)
    val todayBirthdays = getTodayBirthDays(birthdays)
    val todayAnniversaries = getTodayAnniversaries(anniversaries)

    Column(modifier = Modifier.fillMaxSize().padding(10.dp).verticalScroll(rememberScrollState())) {

        Text("Today's Punch", modifier = Modifier.padding(5.dp, 20.dp, 0.dp, 10.dp))
        Row {
            Card(modifier = Modifier.weight(0.5f).padding(5.dp)) {
                Text("In Time", modifier = Modifier.padding(10.dp, 10.dp))
                Text(firstPunchTime, modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 10.dp))
            }
            Card(modifier = Modifier.weight(0.5f).padding(5.dp)) {
                Text("Last Punch", modifier = Modifier.padding(10.dp, 10.dp))
                Text(lastPunchTime, modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 10.dp))
            }
        }

        Text("Leave Balance", modifier = Modifier.padding(5.dp, 20.dp, 0.dp, 10.dp))
        Card(modifier = Modifier.fillMaxWidth().padding(5.dp)) {
            Text("Available Leaves", modifier = Modifier.padding(10.dp, 10.dp))
            Text(balanceLeaves.toString(), modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 10.dp))
        }

        Text(if(todayBirthdays.length() == 0) "No birthdays today" else "Today's Birthdays", modifier = Modifier.padding(5.dp, 20.dp, 0.dp, 10.dp))
        for (index in 0 until todayBirthdays.length()) {
            val event = todayBirthdays.getJSONObject(index)
            Card(modifier = Modifier.fillMaxWidth().padding(5.dp)) {
                Text(event.getString("shortName"), modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 5.dp))
                Text(event.getString("departmentDescription"), modifier = Modifier.padding(10.dp, 5.dp))
                Text(event.getString("dateOfBirth"), modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 10.dp))
            }
        }

        Text(if(todayAnniversaries.length() == 0) "No anniversaries today" else "Today's Anniversaries", modifier = Modifier.padding(5.dp, 20.dp, 0.dp, 10.dp))
        for (index in 0 until todayAnniversaries.length()) {
            val event = todayAnniversaries.getJSONObject(index)
            Card(modifier = Modifier.fillMaxWidth().padding(5.dp)) {
                Text(event.getString("shortName"), modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 5.dp))
                Text(event.getString("departmentDescription"), modifier = Modifier.padding(10.dp, 5.dp))
                Text(event.getString("joiningDate"), modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 10.dp))
            }
        }
    }
}

fun getPunchInTime(punchTimes: JSONArray): String {
    if(punchTimes.length() > 0) {
        val lastObject = punchTimes.getJSONObject(punchTimes.length()-1)
        val punchDate = lastObject.getString("punch_time") as String
        val punchTime = punchDate.split(" ")[1]
        return punchTime
    }
    return "-"
}

fun getLastPunchTime(punchTimes: JSONArray): String {
    if(punchTimes.length() > 0) {
        val lastObject = punchTimes.getJSONObject(0)
        val punchDate = lastObject.getString("punch_time") as String
        val punchTime = punchDate.split(" ")[1]
        return punchTime
    }
    return "-"
}

fun getLeaveBalance(leaveBalance: JSONArray): Double {
    var count = 0.0
    for (i in 0 until leaveBalance.length()) {
        val balanceLeave = leaveBalance.getJSONObject(i).getDouble("balanceLeave")
        count += balanceLeave
    }
    return count
}

fun getTodayBirthDays(birthdays: JSONArray): JSONArray {
    val finalArray = JSONArray()

    val formatter = DateTimeFormatter.ofPattern("MM-dd")
    val current = LocalDateTime.now().format(formatter)

    for (i in 0 until birthdays.length()) {
        val birthday = birthdays.getJSONObject(i)
        val dateOfBirth = birthday.getString("dateOfBirth")
        val date = dateOfBirth.split("T")[0]
        val final = date.split("-")[1] + "-" + date.split("-")[2]
        if(final == current) {
            finalArray.put(birthday)
        }
    }
    return finalArray
}

fun getTodayAnniversaries(anniversaries: JSONArray): JSONArray {

    val finalArray = JSONArray()

    val formatter = DateTimeFormatter.ofPattern("MM-dd")
    val current = LocalDateTime.now().format(formatter)

    for (i in 0 until anniversaries.length()) {
        val anniversary = anniversaries.getJSONObject(i)
        val joiningDate = anniversary.getString("joiningDate")
        val date = joiningDate.split("T")[0]
        val final = date.split("-")[1] + "-" + date.split("-")[2]
        if(final == current) {
            finalArray.put(anniversary)
        }
    }

    return finalArray

}