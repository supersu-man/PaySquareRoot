package dev.sumanth.paysquareroot.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dev.sumanth.paysquareroot.dahboardData
import dev.sumanth.paysquareroot.lmsData
import dev.sumanth.paysquareroot.model.NavigationItem
import dev.sumanth.paysquareroot.punchTimes
import dev.sumanth.paysquareroot.ui.component.BottomBar
import dev.sumanth.paysquareroot.ui.component.TopBar

@Composable
fun HomeScreen() {
    val navigationItems = listOf(
        NavigationItem("Dashboard", Icons.Filled.Home, Icons.Outlined.Home),
        NavigationItem("Punches", Icons.Filled.AccessTime, Icons.Outlined.AccessTime),
        NavigationItem("Leaves", Icons.Filled.CalendarMonth, Icons.Outlined.CalendarMonth),
        NavigationItem("Events", Icons.Filled.Event, Icons.Outlined.Event)
    )
    val pagerState = rememberPagerState( pageCount = { navigationItems.size })
    var topBarText by remember { mutableStateOf("Dashboard") }

    Scaffold(topBar = { TopBar(topBarText) }, bottomBar = { BottomBar(navigationItems, pagerState) }) { innerpadding ->
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize().padding(innerpadding)) { page ->
            topBarText = navigationItems[pagerState.currentPage].title
            if(page == 0) {
                DashboardScreen()
            }
        }
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
