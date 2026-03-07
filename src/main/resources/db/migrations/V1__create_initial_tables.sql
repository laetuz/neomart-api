-- V1__create_initial_tables.sql

CREATE TABLE categories (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE products (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    description TEXT,
    category_id UUID REFERENCES categories(id) ON DELETE SET NULL
);

CREATE TABLE carts (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL, -- THE reference to the neoserver JWT payload
    product_id UUID NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    quantity INT NOT NULL DEFAULT 1
);