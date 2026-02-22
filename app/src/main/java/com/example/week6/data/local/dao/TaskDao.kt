package com.example.week6.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.week6.data.local.entity.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task): Long

    @Query("SELECT * FROM task ORDER BY due_date DESC")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE id = :taskId")
    suspend fun getTaskById(taskId: Int): Task?

    @Query("SELECT * FROM task WHERE done = :done")
    fun getTasksByStatus(done: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE title LIKE '%' || :searchQuery || '%'")
    fun searchTasks(searchQuery: String): Flow<List<Task>>

    @Query("SELECT COUNT(*) FROM task WHERE done = 0")
    fun getPendingTaskCount(): Flow<Int>

    @Update
    suspend fun update(task: Task)

    @Query("UPDATE task SET done = :done WHERE id = :id")
    suspend fun updateTaskStatus(id: Int, done: Boolean)

    @Delete
    suspend fun delete(task: Task)

    @Query("DELETE FROM task WHERE done = 1")
    suspend fun deleteCompletedTasks()
}