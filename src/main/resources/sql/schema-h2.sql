DROP TABLE IF EXISTS customers_employers;
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS employers;

-- CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE customers
(
    id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    age   INTEGER      NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    name  VARCHAR(255) NOT NULL
);

CREATE TABLE accounts
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    balance     FLOAT        NOT NULL,
    currency    VARCHAR(3)   NOT NULL CHECK (currency IN ('USD', 'EUR', 'UAH', 'CHF', 'GBP')),
    number      VARCHAR(255) NOT NULL UNIQUE,
    customer_id BIGINT,
    FOREIGN KEY (customer_id) REFERENCES customers (id)
);

CREATE TABLE employers
(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL
);

CREATE TABLE customers_employers
(
    customer_id BIGINT NOT NULL,
    employer_id BIGINT NOT NULL,
    PRIMARY KEY (customer_id, employer_id),
    FOREIGN KEY (customer_id) REFERENCES customers (id) ON DELETE CASCADE,
    FOREIGN KEY (employer_id) REFERENCES employers (id) ON DELETE CASCADE
);