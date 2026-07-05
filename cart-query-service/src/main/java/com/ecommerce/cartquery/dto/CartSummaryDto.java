package com.ecommerce.cartquery.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartSummaryDto {
    @JsonProperty("totalPrice")
    private String totalPrice;

    @JsonProperty("items")
    private List<CartItemDto> items;
}
