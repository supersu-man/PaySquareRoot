package dev.sumanth.paysquareroot.ui.screen

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
import androidx.compose.ui.unit.dp
import dev.sumanth.paysquareroot.leaves

@Composable
fun LeavesScreen() {

    Column(modifier = Modifier.fillMaxSize().padding(20.dp, 0.dp, 20.dp, 0.dp).verticalScroll(rememberScrollState())) {

        Box(modifier = Modifier.height(30.dp)) {  }

        Text("Leave Balance")

        for (i in 0 until leaves.length()) {
            val obj = leaves.getJSONObject(i)
            Card(modifier = Modifier.fillMaxWidth().padding(0.dp, 10.dp, 0.dp, 0.dp)) {
                Text(obj.getString("leaveDescription"), modifier = Modifier.padding(10.dp, 10.dp))
                Row(modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 10.dp)) {
                    Text("Opening: ${obj.getString("leaveOpeningBalance")}", modifier = Modifier.weight(0.5f))
                    Text("Closing: ${obj.getString("balanceLeave")}", modifier = Modifier.weight(0.55f))
                }
            }
        }

        Box(modifier = Modifier.height(30.dp)) {  }

    }

}