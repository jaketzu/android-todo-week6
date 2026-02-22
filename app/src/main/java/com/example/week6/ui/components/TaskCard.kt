package com.example.week6.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.week6.data.local.entity.Task
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun TaskCard(
    task: Task,
    formatter: SimpleDateFormat,
    onSelect: (Task) -> Unit,
    onToggle: (Task) -> Unit,
    onRemove: (Task) -> Unit
) {
    Card(
        Modifier
            .fillMaxWidth()
            .clickable {
                onSelect(task)
            }) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                Modifier.fillMaxWidth(0.75f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    task.title,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (task.done) TextDecoration.LineThrough else null
                )
                Text(
                    "${task.priority}    ${formatter.format(Date(task.dueDate))}",
                    style = MaterialTheme.typography.bodyMedium
                )
                if (task.description.isNotBlank()) {
                    Text(task.description, style = MaterialTheme.typography.bodyMedium)
                }
            }

            Checkbox(checked = task.done, onCheckedChange = {
                onToggle(task)
            })

            IconButton(
                onClick = {
                    onRemove(task)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove task",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}