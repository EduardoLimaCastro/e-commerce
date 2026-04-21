CREATE TABLE IF NOT EXISTS users(
    id UUID PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255)  NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);