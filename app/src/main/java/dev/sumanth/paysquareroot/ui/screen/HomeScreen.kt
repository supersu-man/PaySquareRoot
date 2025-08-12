package dev.sumanth.paysquareroot.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.sumanth.paysquareroot.model.NavigationItem
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
            } else if(page == 1) {
                PunchesScreen()
            } else if(page == 2) {
                LeavesScreen()
            }
        }
    }
}