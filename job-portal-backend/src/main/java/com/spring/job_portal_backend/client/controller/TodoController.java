package com.spring.job_portal_backend.client.controller;

import com.spring.job_portal_backend.client.service.RestClientTodoService;
import com.spring.job_portal_backend.client.service.TodoService;
import com.spring.job_portal_backend.dto.TodoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="/todos")
public class TodoController {

    private final RestClientTodoService restClientTodoService;
    private final TodoService todoService;

    @GetMapping(version = "1.0")
    ResponseEntity<List<TodoDto>> findAll() {
        //return ResponseEntity.ok(restClientTodoService.findAll());
        return ResponseEntity.ok(todoService.findAll());
    }

    @GetMapping(path="/{id}", version = "1.0")
    ResponseEntity<TodoDto> findById(@PathVariable Long id) {
        //return ResponseEntity.ok(restClientTodoService.findById(id));
        return ResponseEntity.ok(todoService.findById(id));
    }

    @PostMapping(version = "1.0")
    ResponseEntity<TodoDto> create(@RequestBody TodoDto toDoDto) {
        //return ResponseEntity.status(HttpStatus.CREATED).body(restClientTodoService.create(toDoDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(todoService.create(toDoDto));
    }

    @PutMapping(path="/{id}", version = "1.0")
    ResponseEntity<TodoDto> update(@PathVariable Long id, @RequestBody TodoDto toDoDto) {
        //return ResponseEntity.status(HttpStatus.OK).body(restClientTodoService.update(id, toDoDto));
        return ResponseEntity.status(HttpStatus.OK).body(todoService.update(id, toDoDto));
    }

    @DeleteMapping(path="/{id}", version = "1.0")
    ResponseEntity<String> delete(@PathVariable Long id) {
        //restClientTodoService.delete(id);
        todoService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("ToDo deleted successfully");
    }
}
