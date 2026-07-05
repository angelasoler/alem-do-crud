package com.ecommerce.cartquery.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {
    @JsonProperty("productId")
    private String productId;

    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("price")
    private String price;

    @JsonProperty("itemType")
    private String itemType;

    @JsonProperty("timestamp")
    private String timestamp;
}
