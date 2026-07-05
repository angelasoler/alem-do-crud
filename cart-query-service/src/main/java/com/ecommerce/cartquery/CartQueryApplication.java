package com.ecommerce.cartquery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CartQueryApplication {

    public static void main(String[] args) {
        SpringApplication.run(CartQueryApplication.class, args);
    }
}
