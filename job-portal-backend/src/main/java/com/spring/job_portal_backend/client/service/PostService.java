package com.spring.job_portal_backend.client.service;

import com.spring.job_portal_backend.dto.PostDto;
import com.spring.job_portal_backend.dto.TodoDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.*;

import java.util.List;

@HttpExchange(url= "https://jsonplaceholder.typicode.com/posts")
public interface PostService {


    @GetExchange
    List<PostDto> findAll();

    @GetExchange(url="/{id}")
    PostDto findById(@PathVariable Long id);

    @PostExchange
    PostDto create(@RequestBody PostDto post);

    @PutExchange(url="/{id}")
    PostDto update(@PathVariable Long id, @RequestBody PostDto post);

    @DeleteExchange(url="/{id}")
    void delete(@PathVariable Long id);
}
