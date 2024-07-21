INSERT INTO customers (name, email, age)
VALUES ('Viktor', 'viktor@gmail.com', 35),
       ('Maksim', 'maksim@gmail.com', 34),
       ('Dan', 'dan@gmail.com', 35);

INSERT INTO accounts (number, currency, balance, customer_id)
values (uuid_generate_v4(), 'USD', 100.0, 1),
       (uuid_generate_v4(), 'USD', 50.0, 2);

INSERT INTO employers (name, address)
VALUES ('Amazon', 'Seattle, USA'),
       ('Apple', 'California'),
       ('Google', 'California');

INSERT INTO customers_employers (customer_id, employer_id)
VALUES (1, 1),
       (2, 2),
       (3, 1);