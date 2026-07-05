CREATE TABLE carts (
    user_id VARCHAR(255) NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (user_id)
);

CREATE TABLE cart_items (
    id BIGSERIAL,
    user_id VARCHAR(255) NOT NULL,
    product_id VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    item_type VARCHAR(50) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);
