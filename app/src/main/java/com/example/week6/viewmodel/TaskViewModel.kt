package com.example.week6.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.week6.data.local.TasksUIState
import com.example.week6.data.local.TodoFilter
import com.example.week6.data.local.entity.Task
import com.example.week6.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(TasksUIState())
    val uiState: StateFlow<TasksUIState> = _uiState.asStateFlow()

    val allTasks: StateFlow<List<Task>> = repository.allTasks
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val pendingCount: StateFlow<Int> = repository.pendingTaskCount
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    fun addTask(task: Task) {
        closeAddDialog()
        viewModelScope.launch { repository.insert(task) }
    }

    fun updateTask(task: Task) {
        closeEditTaskDialog()
        viewModelScope.launch { repository.update(task) }
    }

    fun toggleDone(task: Task) {
        viewModelScope.launch {
            val updated = task.copy(done = !task.done)
            repository.update(updated)
        }
    }

    fun removeTask(task: Task) {
        viewModelScope.launch { repository.delete(task) }
    }

    fun removeCompletedTasks() {
        viewModelScope.launch { repository.deleteCompletedTasks() }
    }

    fun openAddDialog() {
        _uiState.value = _uiState.value.copy(
            addTaskFlag = true
        )
    }

    fun closeAddDialog() {
        _uiState.value = _uiState.value.copy(
            addTaskFlag = false
        )
    }

    fun selectTask(task: Task) {
        _uiState.value = _uiState.value.copy(
            selectedTask = task
        )
    }

    fun closeEditTaskDialog() {
        _uiState.value = _uiState.value.copy(
            selectedTask = null
        )
    }

    fun updateFilter() {
        _uiState.value = _uiState.value.copy(
            filter = when (_uiState.value.filter) {
                TodoFilter.ALL -> TodoFilter.ACTIVE
                TodoFilter.ACTIVE -> TodoFilter.COMPLETED
                else -> TodoFilter.ALL
            }
        )
    }

    fun updateSortType() {
        _uiState.value = _uiState.value.copy(
            sort = !_uiState.value.sort
        )
    }

    fun getFilteredTasks(): List<Task> {
        val filteredTasks = when (_uiState.value.filter) {
            TodoFilter.ACTIVE -> allTasks.value.filter { !it.done }
            TodoFilter.COMPLETED -> allTasks.value.filter { it.done }
            else -> allTasks.value
        }

        return if (_uiState.value.sort) filteredTasks.sortedBy { it.dueDate } else filteredTasks.sortedByDescending { it.dueDate }
    }

    fun closeError() {
        _uiState.value = _uiState.value.copy(
            error = null
        )
    }

    fun toggleDarkTheme() {
        _uiState.value = _uiState.value.copy(
            isDarkTheme = !_uiState.value.isDarkTheme
        )
    }
}