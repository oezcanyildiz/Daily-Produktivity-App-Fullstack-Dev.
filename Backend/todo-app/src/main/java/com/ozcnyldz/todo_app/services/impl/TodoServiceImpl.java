package com.ozcnyldz.todo_app.services.impl;

import java.time.LocalDate;

import java.time.YearMonth;
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
	public Long getCurrentUserId() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails cud = (CustomUserDetails) auth.getPrincipal();
		return cud.getUser().getId();
		}

	@Override
	public Todo createTodo(Todo todo) {
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
	    Long userId = getCurrentUserId();

	    // Zuerst das Todo aus der DB holen
	    Todo todoFromDb = todoRepository.findById(todoId)
	        .orElseThrow(() -> new IllegalArgumentException("Todo nicht gefunden"));

	    // Prüfen, ob das Todo zum aktuell eingeloggten User gehört
	    if (!todoFromDb.getUser().getId().equals(userId)) {
	        throw new IllegalArgumentException("Sie dürfen dieses Todo nicht bearbeiten");
	    }

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
		Long userId=getCurrentUserId();
		
	}

	@Override
	public Todo toggleDone(Long todoId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Todo moveTodo(Long todoId, LocalDate newDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Todo> getTodosByDate( LocalDate date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Todo> getTodosByMonth( YearMonth month) {
		// TODO Auto-generated method stub
		return null;
	}

}
