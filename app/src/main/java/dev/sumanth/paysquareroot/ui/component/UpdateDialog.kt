package dev.sumanth.paysquareroot.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun UpdateDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        title = { Text("New update found") },
        text = { Text(text = "Would you like to download new apk?") },
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text(text = "Download")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = "Cancel")
            }
        }
    )
}