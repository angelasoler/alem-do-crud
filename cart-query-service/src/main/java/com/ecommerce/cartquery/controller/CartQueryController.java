package com.ecommerce.cartquery.controller;

import com.ecommerce.cartquery.dto.CartSummaryDto;
import com.ecommerce.cartquery.service.CartQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartQueryController {

    private final CartQueryService cartQueryService;

    @GetMapping
    public CartSummaryDto getCartSummary(@RequestHeader("X-User-Id") String userId) {
        return cartQueryService.getCartSummary(userId);
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "Cart Query service is up and running!";
    }
}
