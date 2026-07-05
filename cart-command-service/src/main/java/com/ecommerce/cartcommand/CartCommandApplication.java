package com.ecommerce.cartcommand;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CartCommandApplication {
    public static void main(String[] args) {
        SpringApplication.run(CartCommandApplication.class, args);
    }
}
