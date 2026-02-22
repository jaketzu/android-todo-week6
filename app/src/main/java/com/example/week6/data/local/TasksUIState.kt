package com.example.week6.data.local

import com.example.week6.data.local.entity.Task

data class TasksUIState(
    val selectedTask: Task? = null,
    val addTaskFlag: Boolean = false,
    val error: String? = null,
    val filter: TodoFilter = TodoFilter.ALL,
    val sort: Boolean = false,
    val isDarkTheme: Boolean = false
)

enum class TodoFilter {
    ALL, ACTIVE, COMPLETED
}
