package com.ecommerce.apigateway.config;

import com.ecommerce.apigateway.filter.JwtAuthenticationGatewayFilterFactory;
import com.ecommerce.apigateway.filter.UserExistenceGatewayFilterFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GatewayConfig {

    private final JwtAuthenticationGatewayFilterFactory jwtAuthFilter;
    private final UserExistenceGatewayFilterFactory userExistenceFilter;

    public GatewayConfig(JwtAuthenticationGatewayFilterFactory jwtAuthFilter, 
                        UserExistenceGatewayFilterFactory userExistenceFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userExistenceFilter = userExistenceFilter;
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service-public", r -> r
                        .path("/users/register", "/users/login")
                        .uri("lb://user-service"))
                
                .route("user-service-protected", r -> r
                        .path("/users/**")
                        .filters(f -> f
                                .filter(jwtAuthFilter.apply(c -> {}))
                                .filter(userExistenceFilter.apply(c -> {})))
                        .uri("lb://user-service"))
                
                .route("cart-command-service", r -> r
                        .path("/cart/add-item", "/cart/remove-item")
                        .filters(f -> f
                                .filter(jwtAuthFilter.apply(c -> {}))
                                .filter(userExistenceFilter.apply(c -> {})))
                        .uri("lb://cart-command-service"))
                
                .route("cart-query-service", r -> r
                        .path("/cart/**")
                        .filters(f -> f
                                .filter(jwtAuthFilter.apply(c -> {}))
                                .filter(userExistenceFilter.apply(c -> {})))
                        .uri("lb://cart-query-service"))
                
                .route("health-check", r -> r
                        .path("/health")
                        .uri("lb://api-gateway"))
                        
                .build();
    }
}
