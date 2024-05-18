CREATE SCHEMA IF NOT EXISTS bank_api;

CREATE TABLE IF NOT EXISTS bank_api.t_user
(
    id              UUID PRIMARY KEY,
    login           VARCHAR(255) UNIQUE NOT NULL,
    password        VARCHAR(255)       NOT NULL,
    full_name       VARCHAR(100)       NOT NULL,
    date_of_birth   DATE               NOT NULL,
    c_role            VARCHAR(25)        NOT NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS bank_api.t_account
(
    id UUID PRIMARY KEY,
    user_id UUID REFERENCES bank_api.t_user(id) ON DELETE CASCADE,
    card VARCHAR(255) UNIQUE,
    initial_deposit NUMERIC(15, 2)     NOT NULL,
    current_balance NUMERIC(15, 2)     NOT NULL CHECK (current_balance >= 0)
    );

CREATE TABLE IF NOT EXISTS bank_api.t_phone
(
    id      UUID PRIMARY KEY,
    user_id UUID REFERENCES bank_api.t_user (id) ON DELETE CASCADE,
    phone   VARCHAR(20) UNIQUE NOT NULL
    );

CREATE TABLE IF NOT EXISTS bank_api.t_email
(
    id      UUID PRIMARY KEY,
    user_id UUID REFERENCES bank_api.t_user (id) ON DELETE CASCADE,
    email   VARCHAR(100) UNIQUE NOT NULL
    );

CREATE TABLE IF NOT EXISTS bank_api.t_transaction
(
    id           UUID PRIMARY KEY,
    from_user_card VARCHAR(255)         NOT NULL REFERENCES bank_api.t_account (card),
    to_user_card   VARCHAR(255)         NOT NULL REFERENCES bank_api.t_account (card),
    amount       NUMERIC(15, 2) NOT NULL CHECK (amount > 0),
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

