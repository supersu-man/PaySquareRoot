package dev.sumanth.paysquareroot.ui.component

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import dev.sumanth.paysquareroot.ui.theme.AppTheme

@Composable
fun Background (callback: @Composable () -> Unit) {
    AppTheme {
        Surface {
            callback()
        }
    }
}