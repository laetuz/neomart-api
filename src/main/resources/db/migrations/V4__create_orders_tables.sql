CREATE TABLE orders (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    total_amount DOUBLE PRECISION NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE order_items (
    id UUID PRIMARY KEY,
    order_id UUID NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    product_id UUID NOT NULL REFERENCES products(id),
    quantity INT NOT NULL,
    price_at_purchase DOUBLE PRECISION NOT NULL
);