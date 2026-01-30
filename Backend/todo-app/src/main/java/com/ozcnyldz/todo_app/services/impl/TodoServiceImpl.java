package com.ozcnyldz.todo_app.services.impl;

import java.time.LocalDate;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ozcnyldz.todo_app.config.CustomUserDetails;
import com.ozcnyldz.todo_app.entities.Todo;
import com.ozcnyldz.todo_app.entities.User;
import com.ozcnyldz.todo_app.repository.TodoRepository;
import com.ozcnyldz.todo_app.repository.UserRepository;
import com.ozcnyldz.todo_app.services.ITodoServices;

@Service
public class TodoServiceImpl implements ITodoServices {
	
	private final TodoRepository todoRepository;
	private final UserRepository userRepository;
	
	public TodoServiceImpl(TodoRepository todoRepository,UserRepository userRepository) {
			this.todoRepository = todoRepository;
			this.userRepository=userRepository;
			
	}
	
	//Meine Helper Methoden für Wiederholende Logik
	public Long getCurrentUserId() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails cud = (CustomUserDetails) auth.getPrincipal();
		return cud.getUser().getId();
		}
	
	private Todo getTodoForCurrentUser(Long todoId) {
	    Long userId = getCurrentUserId();

	    Todo todo = todoRepository.findById(todoId)
	        .orElseThrow(() -> new IllegalArgumentException("Todo nicht gefunden"));

	    if (!todo.getUser().getId().equals(userId)) {
	        throw new IllegalArgumentException("Kein Zugriff auf dieses Todo");
	    }

	    return todo;
	}

	@Override
	public Todo createTodo(Todo todo) {
		
		// Validierungen
		if(todo.getTitle() == null || todo.getTitle().isBlank()){
			throw new IllegalArgumentException("Title kann nicht leer sein");
		}
		if(todo.getDescription()== null || todo.getDescription().isBlank()) {
			throw new IllegalArgumentException("Daily kann nicht leer sein");
		}
		if (todo.getDate() == null) {
		    todo.setDate(LocalDate.now());
		}		
		
		Long userId = getCurrentUserId();
		User user = userRepository.findById(userId)
		.orElseThrow(() -> new IllegalArgumentException("User nicht gefunden"));

		todo.setUser(user);
		return todoRepository.save(todo);
	}
	
	@Override
	public Todo updateTodo(Long todoId, Todo updatedTodo) {
		
		Todo todoFromDb = getTodoForCurrentUser(todoId);

	    // Validierungen
	    if (updatedTodo.getTitle() == null || updatedTodo.getTitle().isBlank()) {
	        throw new IllegalArgumentException("Title kann nicht leer sein");
	    }
	    if (updatedTodo.getDescription() == null || updatedTodo.getDescription().isBlank()) {
	        throw new IllegalArgumentException("Daily kann nicht leer sein");
	    }
	    if (updatedTodo.getDate() == null) {
	        updatedTodo.setDate(LocalDate.now());
	    }

	    // Die Felder des bestehenden Todos aktualisieren
	    todoFromDb.setTitle(updatedTodo.getTitle());
	    todoFromDb.setDescription(updatedTodo.getDescription());
	    todoFromDb.setDate(updatedTodo.getDate());
	    todoFromDb.setDueTime(updatedTodo.getDueTime());
	    todoFromDb.setDone(updatedTodo.isDone());

	    return todoRepository.save(todoFromDb);
	}

	@Override
	public void deleteTodo(Long todoId) {
		
		Todo todoFromDb = getTodoForCurrentUser(todoId);
		
		todoRepository.delete(todoFromDb);
		
	}

	@Override
	public Todo toggleDone(Long todoId) {
		Todo todoFromDb=getTodoForCurrentUser(todoId);
		
		todoFromDb.setDone(!todoFromDb.isDone());
		return todoRepository.save(todoFromDb);
	}

	@Override
	public Todo moveTodo(Long todoId, LocalDate newDate) {
		Todo todoFromDb=getTodoForCurrentUser(todoId);
		
		todoFromDb.setDate(newDate);
		return todoRepository.save(todoFromDb);
	}

	@Override
	public List<Todo> getTodosByDate(LocalDate date) {
	    Long userId = getCurrentUserId();

	    date = LocalDate.now();
	    
	    List<Todo> userDayTodos=todoRepository.findAllByUserIdAndDate(userId, date);
		return userDayTodos;
	}


}
