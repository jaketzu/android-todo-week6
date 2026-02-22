package com.example.week6.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.week6.data.local.entity.Task
import com.example.week6.ui.components.TaskCard
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    tasks: List<Task>,
    onGoBack: () -> Unit,
    formatter: SimpleDateFormat,
    onSelectTask: (Task) -> Unit,
    onToggleTask: (Task) -> Unit,
    onRemoveTask: (Task) -> Unit
) {
    val groupedTasks = tasks.sortedBy { it.dueDate }.groupBy { it.dueDate }

    Column(modifier = Modifier.padding(8.dp)) {
        TopAppBar(
            title = { Text("Calendar") },
            navigationIcon = {
                IconButton(onClick = onGoBack) {
                    Icon(Icons.Filled.ArrowBackIosNew, contentDescription = "Go to home screen")
                }
            }
        )

        LazyColumn(
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            groupedTasks.forEach { (date, tasks) ->
                item {
                    Text(
                        text = formatter.format(date),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                }

                items(tasks) { task ->
                    TaskCard(
                        task = task,
                        formatter = formatter,
                        onSelect = { onSelectTask(task) },
                        onToggle = onToggleTask,
                        onRemove = onRemoveTask
                    )
                }
            }
        }
    }
}
