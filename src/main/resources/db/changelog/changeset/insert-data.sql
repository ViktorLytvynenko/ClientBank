INSERT INTO users(enabled, encrypted_password, user_name)
VALUES (true, '$2a$10$BXH1wlAJPIMXvjnJTBoRuea4CvZwSs8/Zqz4bDRZBDJ6hxvXoHlqq', 'user'),
       (true, '$2a$10$BXH1wlAJPIMXvjnJTBoRuea4CvZwSs8/Zqz4bDRZBDJ6hxvXoHlqq', 'admin');

INSERT INTO roles(role_id, role_name, user_id)
VALUES (101, 'USER', 1),
       (102, 'ADMIN', 2);

INSERT INTO customers (name, email, age, password, phone, created_date, last_modified_date)
VALUES ('Viktor', 'viktor@gmail.com', 35, 'qWerty', '+1234567890', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Maksim', 'maksim@gmail.com', 34, 'qWerty', '+0987654321', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Dan', 'dan@gmail.com', 35, 'qWerty', '+1122334455', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Aleksandr', 'alexander@gmail.com', 28, 'qWerty', '+1234509876', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Sergey', 'sergey@gmail.com', 42, 'qWerty', '+1987654321', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Anna', 'anna@gmail.com', 31, 'qWerty', '+1029384756', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Ekaterina', 'ekaterina@gmail.com', 29, 'qWerty', '+1123581321', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Ivan', 'ivan@gmail.com', 40, 'qWerty', '+1029384765', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Olga', 'olga@gmail.com', 36, 'qWerty', '+1092837465', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Dmitry', 'dmitry@gmail.com', 33, 'qWerty', '+1928374650', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Maria', 'maria@gmail.com', 27, 'qWerty', '+1234098765', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Alexey', 'alexey@gmail.com', 39, 'qWerty', '+1092847563', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Karina', 'karina@gmail.com', 25, 'qWerty', '+1982736450', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Nikolay', 'nikolay@gmail.com', 38, 'qWerty', '+1234987650', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Yulia', 'julia@gmail.com', 26, 'qWerty', '+1123456789', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Anastasia', 'anastasia@gmail.com', 32, 'qWerty', '+1982345670', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Vladimir', 'vladimir@gmail.com', 45, 'qWerty', '+1092837465', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- INSERT INTO accounts (number, currency, balance, customer_id, created_date, last_modified_date)
-- VALUES (uuid_generate_v4(), 'USD', 100.0, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
--        (uuid_generate_v4(), 'USD', 50.0, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO employers (name, address, created_date, last_modified_date)
VALUES ('Amazon', 'Seattle, USA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Apple', 'California, USA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Google', 'California, USA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO customers_employers (customer_id, employer_id)
VALUES (1, 1),
       (2, 2),
       (3, 1);
