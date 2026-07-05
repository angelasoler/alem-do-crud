package com.ecommerce.cartcommand.controller;

import com.ecommerce.cartcommand.dto.AddItemRequest;
import com.ecommerce.cartcommand.dto.RemoveItemRequest;
import com.ecommerce.cartcommand.service.CartCommandService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartCommandController {

    private final CartCommandService cartCommandService;

    @PostMapping("/add-item")
    @ResponseStatus(HttpStatus.CREATED)
    public void addItem(@RequestHeader("X-User-Id") String userId, @Valid @RequestBody AddItemRequest addItemRequest) {
        cartCommandService.addItem(userId, addItemRequest);
    }

    @PostMapping("/remove-item")
    @ResponseStatus(HttpStatus.CREATED)
    public void removeItem(@RequestHeader("X-User-Id") String userId, @Valid @RequestBody RemoveItemRequest removeItemRequest) {
        cartCommandService.removeItem(userId, removeItemRequest);
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "Cart Command service is up and running!";
    }
}
