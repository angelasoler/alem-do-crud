package com.ecommerce.cartquery.service;

import com.ecommerce.cartquery.dto.CartSummaryDto;

public interface CartQueryService {
    CartSummaryDto getCartSummary(String userId);
}
