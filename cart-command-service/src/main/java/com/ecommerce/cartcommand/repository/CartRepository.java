package com.ecommerce.cartcommand.repository;

import com.ecommerce.cartcommand.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {}
