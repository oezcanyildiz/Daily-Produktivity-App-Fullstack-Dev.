package com.ozcnyldz.todo_app.services;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import com.ozcnyldz.todo_app.entities.Todo;

public interface ITodoServices {
	
	Todo createTodo(Todo todo);

	Todo updateTodo(Long todoId, Todo updatedTodo);

	void deleteTodo(Long todoId);
	
	Todo toggleDone(Long todoId);

	Todo moveTodo(Long todoId,  LocalDate newDate);

	List<Todo> getTodosByDate( LocalDate date);

	List<Todo> getTodosByMonth( YearMonth month);

}
