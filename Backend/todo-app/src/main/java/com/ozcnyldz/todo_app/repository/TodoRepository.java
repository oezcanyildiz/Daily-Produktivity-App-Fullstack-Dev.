package com.ozcnyldz.todo_app.repository;

import java.time.LocalDate;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ozcnyldz.todo_app.entities.Todo;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
	
	List<Todo> findAllByUserIdAndDate(Long userId, LocalDate date);
	

	
}
