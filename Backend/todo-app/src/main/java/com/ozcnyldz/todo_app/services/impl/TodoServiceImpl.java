package com.ozcnyldz.todo_app.services.impl;

import java.time.LocalDate;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ozcnyldz.todo_app.config.CustomUserDetails;
import com.ozcnyldz.todo_app.dto.TodoRequestDTO;
import com.ozcnyldz.todo_app.dto.TodoResponseDTO;
import com.ozcnyldz.todo_app.entities.Todo;
import com.ozcnyldz.todo_app.entities.User;
import com.ozcnyldz.todo_app.repository.TodoRepository;
import com.ozcnyldz.todo_app.repository.UserRepository;
import com.ozcnyldz.todo_app.services.ITodoServices;

@Service
public class TodoServiceImpl implements ITodoServices {

	private final TodoRepository todoRepository;
	private final UserRepository userRepository;

	public TodoServiceImpl(TodoRepository todoRepository, UserRepository userRepository) {
		this.todoRepository = todoRepository;
		this.userRepository = userRepository;

	}

	// Meine Helper Methoden für Wiederholende Logik
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

	private TodoResponseDTO mapToResponseDTO(Todo todo) {
		return new TodoResponseDTO(
				todo.getId(),
				todo.getTitle(),
				todo.getDescription(),
				todo.isDone(),
				todo.getDueTime(),
				todo.getDate());
	}

	private Todo mapToEntity(TodoRequestDTO dto) {
		Todo todo = new Todo();
		todo.setTitle(dto.getTitle());
		todo.setDescription(dto.getDescription());
		todo.setDone(dto.isDone());
		todo.setDueTime(dto.getDueTime());

		todo.setDate(dto.getDate() != null ? dto.getDate() : LocalDate.now());
		return todo;
	}

	@Override
	public TodoResponseDTO createTodo(TodoRequestDTO newTodoDTO) {
		Todo newTodo = mapToEntity(newTodoDTO);

		Long userId = getCurrentUserId();
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("User nicht gefunden"));

		newTodo.setUser(user);
		Todo savedTodo = todoRepository.save(newTodo);

		return mapToResponseDTO(savedTodo);
	}

	@Override
	public TodoResponseDTO updateTodo(Long todoId, TodoRequestDTO updatedTodoDTO) {
		Todo todoFromDb = getTodoForCurrentUser(todoId);

		// Falls im DTO kein Datum gesetzt, heute nehmen
		LocalDate date = updatedTodoDTO.getDate() != null ? updatedTodoDTO.getDate() : LocalDate.now();

		todoFromDb.setTitle(updatedTodoDTO.getTitle());
		todoFromDb.setDescription(updatedTodoDTO.getDescription());
		todoFromDb.setDate(date);
		todoFromDb.setDueTime(updatedTodoDTO.getDueTime());
		todoFromDb.setDone(updatedTodoDTO.isDone());

		Todo updated = todoRepository.save(todoFromDb);
		return mapToResponseDTO(updated);
	}

	@Override
	public TodoResponseDTO toggleDone(Long id) {
		Todo todo = getTodoForCurrentUser(id);
		todo.setDone(!todo.isDone());
		return mapToResponseDTO(todoRepository.save(todo));
	}

	@Override
	public TodoResponseDTO moveTodo(Long id, LocalDate newDate) {
		Todo todo = getTodoForCurrentUser(id);
		todo.setDate(newDate);
		return mapToResponseDTO(todoRepository.save(todo));
	}

	@Override
	public List<TodoResponseDTO> getTodayTodos() {
		return getTodosByDate(LocalDate.now());
	}

	@Override
	public List<TodoResponseDTO> getTodosByDate(LocalDate date) {
		Long userId = getCurrentUserId();
		// System.out.println("DEBUG: getTodosByDate called for userId: " + userId + ",
		// Date: " + date);
		List<Todo> todos = todoRepository.findAllByUserIdAndDate(userId, date);
		return todos.stream()
				.map(this::mapToResponseDTO)
				.toList();
	}

	@Override
	public void deleteTodo(Long id) {
		Todo todo = getTodoForCurrentUser(id);
		todoRepository.delete(todo);

	}
}
