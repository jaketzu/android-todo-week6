package com.example.week6.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ErrorDialog(
    error: String,
    onClose: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onClose, title = { Text("Error") },
        text = {
            Text(error)
        },
        confirmButton = {},
        dismissButton = {
            Button(onClick = onClose) {
                Text("OK")
            }
        },
    )
}