package com.example.week6.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String = "",

    @ColumnInfo(name = "priority")
    val priority: Priority = Priority.LOW,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "due_date")
    val dueDate: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "done")
    val done: Boolean = false
) {
    enum class Priority {
        LOW,
        MEDIUM,
        HIGH
    }
}