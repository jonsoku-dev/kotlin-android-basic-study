package com.example.study.todo

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTodo(todo: Todo)

    @Query("SELECT * FROM todo ORDER BY id ASC")
    fun readTodos(): LiveData<List<Todo>>

    @Query("DELETE FROM todo WHERE id = :id")
    suspend fun deleteTodoById(id: Long)
}