package com.ecommerce.cartcommand.service;

import com.ecommerce.cartcommand.dto.AddItemRequest;
import com.ecommerce.cartcommand.dto.RemoveItemRequest;

public interface CartCommandService {
    void addItem(String userId, AddItemRequest addItemRequest);
    void removeItem(String userId, RemoveItemRequest removeItemRequest);
}
