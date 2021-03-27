package com.example.study.todo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo")
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val content: String,
    val completed: Boolean = false
)