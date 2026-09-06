package com.spring.job_portal_backend.client.service;

import com.spring.job_portal_backend.dto.TodoDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class RestClientTodoService {

    private final RestClient restClient;
    private static final String TODOS_API = "/todos";

    public RestClientTodoService(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("https://jsonplaceholder.typicode.com")
                .defaultHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                .defaultHeader("Accept","application/json")
                .requestFactory(new JdkClientHttpRequestFactory())
                .build();
    }

    public List<TodoDto> findAll() {
        return restClient.get()
                .uri(TODOS_API)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) ->
                        new IllegalStateException("Failed to fetch todos"))
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public TodoDto findById(Long id) {
        try {
            return restClient.get()
                    .uri(TODOS_API + "/{id}", id)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (req, res) ->
                            new IllegalArgumentException("Todo not found with id: " + id))
                    .body(TodoDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public TodoDto create(TodoDto todo) {
        return restClient.post()
                .uri(TODOS_API)
                .body(todo)
                .retrieve()
                .body(TodoDto.class);
    }

    public TodoDto update(Long id, TodoDto todo) {
        return restClient.put()
                .uri(TODOS_API + "/{id}", id)
                .body(todo)
                .retrieve()
                .body(TodoDto.class);
    }

    public void delete(Long id) {
        restClient.delete()
                .uri(TODOS_API+ "/{id}", id)
                .retrieve()
                .toBodilessEntity();
    }
}
