package com.example.week6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.week6.data.local.AppDatabase
import com.example.week6.data.repository.TaskRepository
import com.example.week6.navigation.ROUTE_CALENDAR
import com.example.week6.navigation.ROUTE_HOME
import com.example.week6.navigation.ROUTE_SETTINGS
import com.example.week6.ui.components.ErrorDialog
import com.example.week6.ui.components.TaskDialog
import com.example.week6.ui.screens.CalendarScreen
import com.example.week6.ui.screens.HomeScreen
import com.example.week6.ui.screens.SettingsScreen
import com.example.week6.ui.theme.Week4Theme
import com.example.week6.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    private val database by lazy { AppDatabase.getDatabase(applicationContext) }

    private val repository by lazy { TaskRepository(database.taskDao()) }

    private val taskViewModel: TaskViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return TaskViewModel(repository) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val uiState by taskViewModel.uiState.collectAsState()
            val tasks by taskViewModel.allTasks.collectAsState()
            val filteredTasks = remember(tasks, uiState.filter, uiState.sort) {
                taskViewModel.getFilteredTasks()
            }
            val pendingCount by taskViewModel.pendingCount.collectAsState()

            val navController = rememberNavController()

            val formatter = SimpleDateFormat("d.M.yyyy", Locale.UK)

            Week4Theme(darkTheme = uiState.isDarkTheme) {
                Scaffold(
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.safeDrawing)
                        .fillMaxSize(),
                    floatingActionButton = {
                        FloatingActionButton(onClick = { taskViewModel.openAddDialog() }) {
                            Icon(Icons.Default.Add, "Add task")
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = ROUTE_HOME,
                        modifier = Modifier.consumeWindowInsets(innerPadding)
                    ) {
                        composable(route = ROUTE_HOME) {
                            HomeScreen(
                                tasks = filteredTasks,
                                pendingCount = pendingCount,
                                onNavigateCalendar = { navController.navigate(ROUTE_CALENDAR) },
                                onNavigateSettings = { navController.navigate(ROUTE_SETTINGS) },
                                formatter = formatter,
                                onPressAdd = { taskViewModel.openAddDialog() },
                                onPressFilter = { taskViewModel.updateFilter() },
                                onPressSort = { taskViewModel.updateSortType() },
                                onSelectTask = { task -> taskViewModel.selectTask(task) },
                                onRemoveTask = { task -> taskViewModel.removeTask(task) },
                                onRemoveDone = { taskViewModel.removeCompletedTasks() },
                                onToggleTask = { task -> taskViewModel.toggleDone(task) }
                            )
                        }

                        composable(route = ROUTE_CALENDAR) {
                            CalendarScreen(
                                tasks = tasks,
                                onGoBack = { navController.popBackStack() },
                                formatter = formatter,
                                onSelectTask = { task -> taskViewModel.selectTask(task) },
                                onToggleTask = { task -> taskViewModel.toggleDone(task) },
                                onRemoveTask = { task -> taskViewModel.removeTask(task) }
                            )
                        }

                        composable(route = ROUTE_SETTINGS) {
                            SettingsScreen(
                                onGoBack = { navController.popBackStack() },
                                isDarkTheme = uiState.isDarkTheme,
                                onToggleTheme = { taskViewModel.toggleDarkTheme() })
                        }
                    }


                    if (uiState.addTaskFlag) {
                        TaskDialog(
                            task = null,
                            onConfirm = { task -> taskViewModel.addTask(task) },
                            onClose = { taskViewModel.closeAddDialog() })
                    }

                    if (uiState.selectedTask != null) {
                        TaskDialog(
                            task = uiState.selectedTask!!,
                            onConfirm = { task -> taskViewModel.updateTask(task) },
                            onClose = { taskViewModel.closeEditTaskDialog() }
                        )
                    }

                    if (uiState.error != null) {
                        ErrorDialog(
                            error = uiState.error!!,
                            onClose = { taskViewModel.closeError() })
                    }
                }
            }
        }
    }
}

