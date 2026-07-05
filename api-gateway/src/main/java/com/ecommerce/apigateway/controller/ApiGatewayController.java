package com.ecommerce.apigateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiGatewayController {

    @GetMapping("/health")
    public String healthCheck() {
        return "API Gateway is up and running!";
    }
    
    @GetMapping("/")
    public String welcome() {
        return "{\"message\": \"Welcome to E-Commerce Cart API Gateway\", \"status\": \"Active\", \"authentication\": \"JWT Required for protected endpoints\"}";
    }
}
