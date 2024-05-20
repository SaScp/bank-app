CREATE SCHEMA IF NOT EXISTS bank_api;

CREATE TABLE IF NOT EXISTS bank_api.t_account
(
    id UUID PRIMARY KEY,
    card VARCHAR(255) UNIQUE,
    initial_deposit NUMERIC(15, 2)     NOT NULL,
    current_balance NUMERIC(15, 2)     NOT NULL CHECK (current_balance >= 0)
    );

CREATE TABLE IF NOT EXISTS bank_api.t_transaction
(
    id           UUID PRIMARY KEY,
    from_user_card VARCHAR(255)         NOT NULL REFERENCES bank_api.t_account (card),
    to_user_card   VARCHAR(255)         NOT NULL REFERENCES bank_api.t_account (card),
    amount       NUMERIC(15, 2) NOT NULL CHECK (amount > 0),
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

