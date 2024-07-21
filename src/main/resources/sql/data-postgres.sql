INSERT INTO customers (name, email, age)
VALUES ('Viktor', 'viktor@gmail.com', 35),
       ('Maksim', 'maksim@gmail.com', 34),
       ('Dan', 'dan@gmail.com', 35);

INSERT INTO accounts (number, currency, balance, customer_id)
values (uuid_generate_v4(), 'USD', 100.0, 1),
       (uuid_generate_v4(), 'USD', 50.0, 2);

