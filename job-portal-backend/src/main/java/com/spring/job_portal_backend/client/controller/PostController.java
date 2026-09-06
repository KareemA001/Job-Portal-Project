package com.spring.job_portal_backend.client.controller;

import com.spring.job_portal_backend.client.service.PostService;
import com.spring.job_portal_backend.dto.PostDto;
import com.spring.job_portal_backend.dto.TodoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="/posts")
public class PostController {

    private final PostService postService;

    @GetMapping(version = "1.0")
    ResponseEntity<List<PostDto>> findAll() {
        return ResponseEntity.ok(postService.findAll());
    }

    @GetMapping(path="/{id}", version = "1.0")
    ResponseEntity<PostDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.findById(id));
    }

    @PostMapping(version = "1.0")
    ResponseEntity<PostDto> create(@RequestBody PostDto postDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.create(postDto));
    }

    @PutMapping(path="/{id}", version = "1.0")
    ResponseEntity<PostDto> update(@PathVariable Long id, @RequestBody PostDto postDto) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.update(id, postDto));
    }

    @DeleteMapping(path="/{id}", version = "1.0")
    ResponseEntity<String> delete(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Post deleted successfully");
    }
}
