package com.ozcnyldz.todo_app.controller.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ozcnyldz.todo_app.controller.ITodoController;
import com.ozcnyldz.todo_app.dto.TodoRequestDTO;
import com.ozcnyldz.todo_app.dto.TodoResponseDTO;
import com.ozcnyldz.todo_app.services.ITodoServices;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/todo")
public class TodoController implements ITodoController {

    private final ITodoServices todoServices;

    public TodoController(ITodoServices todoServices) {
        this.todoServices = todoServices;
    }

    @Override
    @PostMapping("/create")
    public TodoResponseDTO createTodo(@RequestBody @Valid TodoRequestDTO dto) {
        return todoServices.createTodo(dto);
    }

    @Override
    @PutMapping("/update/{id}")
    public TodoResponseDTO updateTodo(
            @PathVariable Long id,
            @RequestBody @Valid TodoRequestDTO dto) {
        return todoServices.updateTodo(id, dto);
    }

    @Override
    @DeleteMapping("/delete/{id}")
    public void deleteTodo(@PathVariable Long id) {
        todoServices.deleteTodo(id);
    }

    @Override
    @PutMapping("/toggle/{id}")
    public TodoResponseDTO toggleDone(@PathVariable Long id) {
        return todoServices.toggleDone(id);
    }

    @Override
    @PutMapping("/move/{id}")
    public TodoResponseDTO moveTodo(
            @PathVariable Long id,
            @RequestParam LocalDate newDate) {
        return todoServices.moveTodo(id, newDate);
    }

    @Override
    @GetMapping("/mydailys/today")
    public List<TodoResponseDTO> getTodayTodos() {
        return todoServices.getTodayTodos();
    }

    @Override
    @GetMapping("/mydailys/date/{date}")
    public List<TodoResponseDTO> getTodosByDate(@PathVariable LocalDate date) {
        return todoServices.getTodosByDate(date);
    }
}
