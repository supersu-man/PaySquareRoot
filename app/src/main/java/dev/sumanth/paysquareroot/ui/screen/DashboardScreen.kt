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
import dev.sumanth.paysquareroot.anniversaries
import dev.sumanth.paysquareroot.birthdays
import dev.sumanth.paysquareroot.companyData
import dev.sumanth.paysquareroot.leaves
import dev.sumanth.paysquareroot.punchTimes
import dev.sumanth.paysquareroot.utils.getFormattedTime
import dev.sumanth.paysquareroot.utils.getTimeDifference
import dev.sumanth.paysquareroot.utils.getTimeObject
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Preview(showBackground = true)
@Composable
fun DashboardScreen() {

    val firstPunchTime = getPunchInTime(punchTimes)
    val lastPunchTime = getLastPunchTime(punchTimes)
    val outTime = getOutTime(punchTimes)
    val balanceLeaves = getLeaveBalance(leaves)
    val todayBirthdays = getTodayBirthDays(birthdays)
    val todayAnniversaries = getTodayAnniversaries(anniversaries)

    Column(modifier = Modifier.fillMaxSize().padding(10.dp).verticalScroll(rememberScrollState())) {

        Text(companyData.getString("userFullName"), modifier = Modifier.padding(5.dp, 20.dp, 0.dp, 10.dp))
        Text("Employee Code: ${companyData.getString("empCode")}")


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
        Row {
            Card(modifier = Modifier.weight(0.5f).padding(5.dp)) {
                Text("Out Time", modifier = Modifier.padding(10.dp, 10.dp))
                Text(outTime?.first ?: "-", modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 10.dp))
            }
            Card(modifier = Modifier.weight(0.5f).padding(5.dp)) {
                Text("Time left", modifier = Modifier.padding(10.dp, 10.dp))
                Text(outTime?.second ?: "-", modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 10.dp))
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
    if(punchTimes.length() == 0) return "-"

    val lastObject = punchTimes.getJSONObject(punchTimes.length()-1)
    val punchDate = lastObject.getString("punch_time") as String
    val punchTime = punchDate.split(" ")[1]
    val timeObj = getTimeObject(punchTime)
    val formattedTime = getFormattedTime(timeObj)
    return formattedTime

}

fun getLastPunchTime(punchTimes: JSONArray): String {
    if(punchTimes.length() == 0) return "-"

    val lastObject = punchTimes.getJSONObject(0)
    val punchDate = lastObject.getString("punch_time") as String
    val punchTime = punchDate.split(" ")[1]
    val timeObj = getTimeObject(punchTime)
    val formattedTime = getFormattedTime(timeObj)
    return formattedTime

}

fun getOutTime(punchTimes: JSONArray): Pair<String, String>? {
    if(punchTimes.length() == 0) return null
    val lastObject = punchTimes.getJSONObject(punchTimes.length()-1)
    val punchDate = lastObject.getString("punch_time") as String
    val punchTime = punchDate.split(" ")[1]
    val timeObj = getTimeObject(punchTime)

    val calendar = Calendar.getInstance()
    calendar.time = timeObj
    calendar.add(Calendar.HOUR, 9)

    val timeDifference = getTimeDifference(Calendar.getInstance().time, calendar.time)
    return Pair(getFormattedTime(calendar.time), timeDifference)
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