package com.ozcnyldz.todo_app.services;

import java.time.LocalDate;

import java.util.List;

import com.ozcnyldz.todo_app.dto.TodoRequestDTO;
import com.ozcnyldz.todo_app.dto.TodoResponseDTO;

public interface ITodoServices {

    // Todo erstellen
    TodoResponseDTO createTodo(TodoRequestDTO newTodo);

    // Todo updaten
    TodoResponseDTO updateTodo(Long id, TodoRequestDTO updatedTodo);

    // Todo löschen
    void deleteTodo(Long id);

    // Todo als erledigt / nicht erledigt markieren
    TodoResponseDTO toggleDone(Long id);

    // Todo auf anderes Datum verschieben
    TodoResponseDTO moveTodo(Long id, LocalDate newDate);

    // Alle Todos für heute abrufen
    List<TodoResponseDTO> getTodayTodos();

    List<TodoResponseDTO> getTodosByDate(LocalDate date); // New method

}
