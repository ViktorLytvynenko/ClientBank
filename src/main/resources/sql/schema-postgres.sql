DROP TABLE IF EXISTS customers_employers;
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS employers;

-- CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table customers
(
    id                 SERIAL PRIMARY KEY,
    age                integer                             NOT NULL,
    email              varchar(255)                        NOT NULL UNIQUE,
    name               varchar(255)                        NOT NULL,
    password           VARCHAR(255)                        NOT NULL,
    phone              VARCHAR(20)                         NOT NULL,
    created_date       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

create table accounts
(
    id                 SERIAL PRIMARY KEY,
    balance            float(53)                           NOT NULL,
    currency           varchar(3)                          NOT NULL CHECK (currency in ('USD', 'EUR', 'UAH', 'CHF', 'GBP')),
    number             varchar(255)                        NOT NULL UNIQUE,
    customer_id        bigint,
    created_date       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers (id)
);

create table employers
(
    id                 SERIAL PRIMARY KEY,
    address            varchar(255)                        NOT NULL,
    name               varchar(255)                        NOT NULL,
    created_date       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE customers_employers
(
    customer_id BIGINT NOT NULL,
    employer_id BIGINT NOT NULL,
    PRIMARY KEY (customer_id, employer_id),
    FOREIGN KEY (customer_id) REFERENCES customers (id) ON DELETE CASCADE,
    FOREIGN KEY (employer_id) REFERENCES employers (id) ON DELETE CASCADE
);