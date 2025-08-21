package dev.sumanth.paysquareroot.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.sumanth.paysquareroot.anniversaries
import dev.sumanth.paysquareroot.birthdays
import dev.sumanth.paysquareroot.companyData
import dev.sumanth.paysquareroot.holidayList
import dev.sumanth.paysquareroot.leaves
import dev.sumanth.paysquareroot.punchTimes
import dev.sumanth.paysquareroot.utils.getDateObject
import dev.sumanth.paysquareroot.utils.getDateObjectV2
import dev.sumanth.paysquareroot.utils.getFormattedDate
import dev.sumanth.paysquareroot.utils.getFormattedDateV2
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

    Column(modifier = Modifier.fillMaxSize().padding(20.dp, 0.dp, 20.dp, 0.dp).verticalScroll(rememberScrollState())) {

        Box(modifier = Modifier.height(30.dp)) {  }

        Text(companyData.getString("userFullName"), fontSize = 30.sp)
        Text("Employee Code: ${companyData.getString("empCode")}")

        Box(modifier = Modifier.height(30.dp)) {  }

        Text("Today's Punch")
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp)) {
            Card(modifier = Modifier.weight(0.5f)) {
                Text("In Time", modifier = Modifier.padding(10.dp, 10.dp))
                Text(firstPunchTime, modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 10.dp))
            }
            Card(modifier = Modifier.weight(0.5f)) {
                Text("Last Punch", modifier = Modifier.padding(10.dp, 10.dp))
                Text(lastPunchTime, modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 10.dp))
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp)) {
            Card(modifier = Modifier.weight(0.5f)) {
                Text("Out Time", modifier = Modifier.padding(10.dp, 10.dp))
                Text(outTime?.first ?: "-", modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 10.dp))
            }
            Card(modifier = Modifier.weight(0.5f)) {
                Text("Time left", modifier = Modifier.padding(10.dp, 10.dp))
                Text(outTime?.second ?: "-", modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 10.dp))
            }
        }

        Box(modifier = Modifier.height(30.dp)) {  }

        Text("Leave Balance")
        Card(modifier = Modifier.fillMaxWidth().padding(0.dp, 10.dp, 0.dp, 0.dp)) {
            Text("Available Leaves", modifier = Modifier.padding(10.dp, 10.dp))
            Text(balanceLeaves.toString(), modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 10.dp))
        }

        Box(modifier = Modifier.height(30.dp)) {  }

        Text(if(todayBirthdays.length() == 0) "No birthdays today" else "Today's Birthdays")
        for (index in 0 until todayBirthdays.length()) {
            val event = todayBirthdays.getJSONObject(index)
            Card(modifier = Modifier.fillMaxWidth().padding(0.dp, 10.dp, 0.dp, 0.dp)) {
                Text(event.getString("shortName"), modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 5.dp))
                Text(event.getString("departmentDescription"), modifier = Modifier.padding(10.dp, 5.dp))
                Text(formattedDate(event.getString("dateOfBirth")) , modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 10.dp))
            }
        }

        Box(modifier = Modifier.height(30.dp)) {  }

        Text(if(todayAnniversaries.length() == 0) "No anniversaries today" else "Today's Anniversaries")
        for (index in 0 until todayAnniversaries.length()) {
            val event = todayAnniversaries.getJSONObject(index)
            Card(modifier = Modifier.fillMaxWidth().padding(0.dp, 10.dp, 0.dp, 0.dp)) {
                Text(event.getString("shortName"), modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 5.dp))
                Text(event.getString("departmentDescription"), modifier = Modifier.padding(10.dp, 5.dp))
                Text(formattedDate(event.getString("joiningDate")), modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 10.dp))
            }
        }

        Box(modifier = Modifier.height(30.dp)) {  }

        Text("Holidays")
        for (index in 0 until holidayList.length()) {
            val holiday = holidayList.getJSONObject(index)
            Card(modifier = Modifier.fillMaxWidth().padding(0.dp, 10.dp, 0.dp, 0.dp)) {
                Text(holiday.getString("holiDay"), modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 5.dp))
                Text(formattedDateV2(holiday.getString("fromDate")), modifier = Modifier.padding(10.dp, 5.dp))
            }
        }
        Box(modifier = Modifier.height(30.dp)) {  }
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

private fun getOutTime(punchTimes: JSONArray): Pair<String, String>? {
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

private fun getLeaveBalance(leaveBalance: JSONArray): Double {
    var count = 0.0
    for (i in 0 until leaveBalance.length()) {
        val balanceLeave = leaveBalance.getJSONObject(i).getDouble("balanceLeave")
        count += balanceLeave
    }
    return count
}

private fun getTodayBirthDays(birthdays: JSONArray): JSONArray {
    val finalArray = JSONArray()
    val today = Calendar.getInstance()
    for (i in 0 until birthdays.length()) {
        val birthday = birthdays.getJSONObject(i)
        val dateOfBirth = birthday.getString("dateOfBirth")
        val date = dateOfBirth.split("T")[0]
        val obj = getDateObject(date)
        if(obj.date == today.get(Calendar.DATE) && obj.month == today.get(Calendar.MONTH)) {
            finalArray.put(birthday)
        }
    }
    return finalArray
}

private fun getTodayAnniversaries(anniversaries: JSONArray): JSONArray {
    val finalArray = JSONArray()
    val today = Calendar.getInstance()
    for (i in 0 until anniversaries.length()) {
        val anniversary = anniversaries.getJSONObject(i)
        val joiningDate = anniversary.getString("joiningDate")
        val date = joiningDate.split("T")[0]
        val obj = getDateObject(date)
        if(obj.date == today.get(Calendar.DATE) && obj.month == today.get(Calendar.MONTH)) {
            finalArray.put(anniversary)
        }
    }
    return finalArray

}

fun formattedDate(s: String): String {
    val date = s.split("T")[0]
    val dateObject = getDateObject(date)
    val formattedDate = getFormattedDate(dateObject)
    return formattedDate
}

fun formattedDateV2(s: String): String {
    val dateObject = getDateObjectV2(s)
    val formattedDate = getFormattedDateV2(dateObject)
    return formattedDate
}