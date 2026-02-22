package com.example.week6.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.week6.data.local.entity.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDialog(
    task: Task?,
    onConfirm: (Task) -> Unit = {},
    onClose: () -> Unit,
) {
    var title by remember { mutableStateOf(task?.title ?: "") }
    var description by remember { mutableStateOf(task?.description ?: "") }
    var selectedIndex by remember {
        mutableIntStateOf(
            when (task?.priority) {
                Task.Priority.MEDIUM -> 1
                Task.Priority.HIGH -> 2
                else -> {
                    0
                }
            }
        )
    }

    var priority by remember { mutableStateOf(task?.priority ?: Task.Priority.LOW) }
    var date by remember { mutableLongStateOf(task?.dueDate ?: System.currentTimeMillis()) }
    val dateState = rememberDatePickerState(
        initialSelectedDateMillis = date,
        initialDisplayMode = DisplayMode.Input
    )

    AlertDialog(
        onDismissRequest = onClose,
        title = { Text("Add task") },
        text = {
            Column {
                TextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") })
                SingleChoiceSegmentedButtonRow {
                    Task.Priority.entries.forEachIndexed { index, label ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = Task.Priority.entries.size
                            ),
                            onClick = {
                                selectedIndex = index
                                priority = label
                            },
                            selected = index == selectedIndex,
                            label = { Text(label.toString()) })
                    }
                }
                DatePicker(
                    state = dateState,
                    title = { Text("Due date") })
            }
        },
        confirmButton = {
            Button(onClick = {
                if (task != null) {
                    onConfirm(
                        task.copy(
                            title = title,
                            description = description,
                            priority = priority,
                            dueDate = dateState.selectedDateMillis!!
                        )
                    )
                } else {
                    onConfirm(
                        Task(
                            title = title,
                            description = description,
                            priority = priority,
                            dueDate = dateState.selectedDateMillis!!,
                        )
                    )
                }
            }, enabled = title.isNotBlank()) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onClose) {
                Text("Cancel")
            }
        }
    )
}