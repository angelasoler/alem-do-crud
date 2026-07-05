package com.ecommerce.apigateway.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {"jwt.secret=testSecretKey123456789012345678901234567890"})
class JwtUtilTest {

    private final JwtUtil jwtUtil = new JwtUtil();

    @Test
    void testJwtUtilCreation() {
        assertNotNull(jwtUtil);
    }

    // More comprehensive tests would require a JWT token generator
    // These tests would be implemented when the User Service is available
}
