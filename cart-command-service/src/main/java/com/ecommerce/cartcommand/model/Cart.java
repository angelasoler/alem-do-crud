package com.ecommerce.cartcommand.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "carts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart implements Serializable {
    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;
}
