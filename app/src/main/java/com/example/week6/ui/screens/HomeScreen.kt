package com.example.week6.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.week6.data.local.entity.Task
import com.example.week6.ui.components.TaskCard
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    tasks: List<Task>,
    pendingCount: Int,
    onNavigateCalendar: () -> Unit,
    onNavigateSettings: () -> Unit,
    formatter: SimpleDateFormat,
    onPressAdd: () -> Unit,
    onPressFilter: () -> Unit,
    onPressSort: () -> Unit,
    onSelectTask: (Task) -> Unit,
    onRemoveTask: (Task) -> Unit,
    onRemoveDone: () -> Unit,
    onToggleTask: (Task) -> Unit
) {

    Column(
        Modifier.padding(12.dp),
    ) {
        TopAppBar(title = { Text("Tasks") }, actions = {
            IconButton(onClick = onNavigateCalendar) {
                Icon(
                    imageVector = Icons.Filled.CalendarMonth, contentDescription = "Go to calendar"
                )
            }

            IconButton(onClick = onNavigateSettings) {
                Icon(
                    imageVector = Icons.Filled.Settings, contentDescription = "Go to settings"
                )
            }
        })

        Button(
            modifier = Modifier.fillMaxWidth(), onClick = onPressAdd
        ) { Text("Add task") }

        Row(
            Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(onClick = {
                onPressFilter()
            }, Modifier.weight(1f)) { Text("Filter by done") }

            Button(onClick = {
                onPressSort()
            }, Modifier.weight(1f)) { Text("Sort by due date") }
        }

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "$pendingCount tasks pending.",
                style = MaterialTheme.typography.bodyMedium
            )

            IconButton(onClick = { onRemoveDone() }) {
                Icon(Icons.Filled.CleaningServices, "Remove done")
            }
        }

        if (tasks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color.Gray
                    )
                    Text("No tasks", color = Color.Gray)
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tasks, key = { it.id }) { task ->
                    TaskCard(
                        task = task,
                        formatter = formatter,
                        onSelect = { onSelectTask(task) },
                        onToggle = { task -> onToggleTask(task) },
                        onRemove = { task -> onRemoveTask(task) }
                    )
                }
            }
        }
    }
}
