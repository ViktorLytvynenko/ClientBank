DROP TABLE IF EXISTS customers_employers;
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS employers;

-- CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table customers
(
    id                 SERIAL PRIMARY KEY,
    age                integer                             not null,
    email              varchar(255)                        not null unique,
    name               varchar(255)                        not null,
    password           VARCHAR(255)                        NOT NULL,
    phone              VARCHAR(20)                         NOT NULL,
    created_date       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

create table accounts
(
    id                 SERIAL PRIMARY KEY,
    balance            float(53)                           not null,
    currency           varchar(3)                          not null check (currency in ('USD', 'EUR', 'UAH', 'CHF', 'GBP')),
    number             varchar(255)                        not null unique,
    customer_id        bigint,
    created_date       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers (id)
);

create table employers
(
    id                 SERIAL PRIMARY KEY,
    address            varchar(255)                        not null,
    name               varchar(255)                        not null,
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