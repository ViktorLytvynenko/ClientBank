INSERT INTO customers (name, email, age)
VALUES ('Viktor', 'viktor@gmail.com', 35),
       ('Maksim', 'maksim@gmail.com', 34),
       ('Dan', 'dan@gmail.com', 35);

INSERT INTO accounts (number, currency, balance, customer_id)
VALUES ('0123456789', 'USD', 100.0, 1),
       ('1234567890', 'USD', 50.0, 2);