package com.example.study.todo

import androidx.lifecycle.LiveData

class TodoRepository(private val todoDao: TodoDao) {
    val readTodos: LiveData<List<Todo>> = todoDao.readTodos()

    suspend fun addTodo(todo: Todo) {
        todoDao.addTodo(todo)
    }

    suspend fun deleteTodoById(id: Long) {
        todoDao.deleteTodoById(id)
    }
}