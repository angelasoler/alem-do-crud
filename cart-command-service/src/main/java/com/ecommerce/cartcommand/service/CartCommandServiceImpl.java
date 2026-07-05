package com.ecommerce.cartcommand.service;

import com.ecommerce.cartcommand.dto.AddItemRequest;
import com.ecommerce.cartcommand.dto.RemoveItemRequest;
import com.ecommerce.cartcommand.model.Cart;
import com.ecommerce.cartcommand.model.CartItem;
import com.ecommerce.cartcommand.model.CartItemType;
import com.ecommerce.cartcommand.repository.CartRepository;
import com.ecommerce.cartcommand.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CartCommandServiceImpl implements CartCommandService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    @Transactional
    public void addItem(String userId, AddItemRequest addItemRequest) {
        Cart cart = cartRepository.findById(userId)
                .orElse(new Cart(userId, BigDecimal.ZERO));

        BigDecimal itemTotal = addItemRequest.getPrice().multiply(BigDecimal.valueOf(addItemRequest.getQuantity()));
        cart.setTotalPrice(cart.getTotalPrice().add(itemTotal));
        
        cartRepository.save(cart);

        recordItemAndUpdateCache(cart, addItemRequest.getProductId(), addItemRequest.getQuantity(), addItemRequest.getPrice(), CartItemType.ADD);
    }

    @Override
    @Transactional
    public void removeItem(String userId, RemoveItemRequest removeItemRequest) {
        Cart cart = cartRepository.findById(userId)
                .orElse(new Cart(userId, BigDecimal.ZERO));

        BigDecimal itemTotal = removeItemRequest.getPrice().multiply(BigDecimal.valueOf(removeItemRequest.getQuantity()));
        
        // Prevent negative total price
        if (cart.getTotalPrice().compareTo(itemTotal) < 0) {
            cart.setTotalPrice(BigDecimal.ZERO);
        } else {
            cart.setTotalPrice(cart.getTotalPrice().subtract(itemTotal));
        }

        cartRepository.save(cart);

        recordItemAndUpdateCache(cart, removeItemRequest.getProductId(), removeItemRequest.getQuantity(), removeItemRequest.getPrice(), CartItemType.REMOVE);
    }

    private void recordItemAndUpdateCache(Cart cart, String productId, Integer quantity, BigDecimal price, CartItemType itemType) {
        CartItem cartItem = new CartItem();
        cartItem.setUserId(cart.getUserId());
        cartItem.setProductId(productId);
        cartItem.setQuantity(quantity);
        cartItem.setPrice(price);
        cartItem.setTimestamp(LocalDateTime.now());
        cartItem.setItemType(itemType);
        
        cartItemRepository.save(cartItem);

        // Push to cache
        String cartKey = "user:" + cart.getUserId() + ":cart_summary";
        redisTemplate.opsForValue().set(cartKey, cart.getTotalPrice().toString());

        String itemsKey = "user:" + cart.getUserId() + ":cart_items";
        redisTemplate.opsForList().leftPush(itemsKey, cartItem);
    }
}
