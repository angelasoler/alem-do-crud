package com.ecommerce.cartquery.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ecommerce.cartquery.dto.CartSummaryDto;
import com.ecommerce.cartquery.dto.RedisCartItemDto;
import com.ecommerce.cartquery.dto.CartItemDto;
import com.ecommerce.cartquery.service.CartQueryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartQueryServiceImpl implements CartQueryService {

    private static final Logger logger = LoggerFactory.getLogger(CartQueryServiceImpl.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public CartSummaryDto getCartSummary(String userId) {
        BigDecimal totalPrice = getTotalPriceFromRedis(userId);
        List<CartItemDto> items = getItemsFromRedis(userId);

        String formattedTotal = totalPrice.setScale(2, RoundingMode.HALF_UP).toString();
        return new CartSummaryDto(formattedTotal, items);
    }

    private BigDecimal getTotalPriceFromRedis(String userId) {
        String cartKey = "user:" + userId + ":cart_summary";
        try {
            Object totalObj = redisTemplate.opsForValue().get(cartKey);
            if (totalObj != null) {
                return new BigDecimal(String.valueOf(totalObj));
            }
        } catch (Exception e) {
            logger.error("Error parsing total price for userId: {}", userId, e);
        }
        return BigDecimal.ZERO;
    }

    private List<CartItemDto> getItemsFromRedis(String userId) {
        String itemsKey = "user:" + userId + ":cart_items";
        List<Object> itemsObjList = redisTemplate.opsForList().range(itemsKey, 0, -1);

        if (itemsObjList == null || itemsObjList.isEmpty()) {
            return Collections.emptyList();
        }

        return itemsObjList.stream()
                .map(itemObj -> {
                    try {
                        return deserializeAndMapCartItem(itemObj);
                    } catch (Exception e) {
                        logger.error("Error mapping cart item: {}", itemObj, e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private CartItemDto deserializeAndMapCartItem(Object itemObj) {
        RedisCartItemDto redisDto = objectMapper.convertValue(itemObj, RedisCartItemDto.class);
        CartItemDto responseDto = new CartItemDto();
        responseDto.setProductId(redisDto.getProductId());
        responseDto.setQuantity(redisDto.getQuantity());
        responseDto.setPrice(redisDto.getPrice().setScale(2, RoundingMode.HALF_UP).toString());
        responseDto.setItemType(redisDto.getItemType());
        responseDto.setTimestamp(redisDto.getTimestamp().format(formatter));
        return responseDto;
    }
}
