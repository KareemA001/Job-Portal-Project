package com.spring.job_portal_backend.client.config;


import com.spring.job_portal_backend.client.service.PostService;
import com.spring.job_portal_backend.client.service.TodoService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.service.registry.ImportHttpServices;

@Configuration
@ImportHttpServices(types = {TodoService.class, PostService.class})
public class HttpServiceClientConfig {
}
