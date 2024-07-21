INSERT INTO customers (name, email, age) VALUES ('Viktor', 'viktor@gmail.com', 35);
INSERT INTO customers (name, email, age) VALUES ('Maksim', 'maksim@gmail.com', 34);
INSERT INTO customers (name, email, age) VALUES ('Dan', 'dan@gmail.com', 35);

INSERT INTO accounts (number, currency, balance, customer_id) values (uuid_generate_v4(), 'USD', 100.0, 1);

