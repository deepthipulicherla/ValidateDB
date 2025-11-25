
USE appdb;

--CREATE USER 'testuser'@'%' IDENTIFIED BY 'testpass';
--GRANT ALL PRIVILEGES ON appdb.* TO 'testuser'@'%';
--FLUSH PRIVILEGES;

CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(255) NOT NULL,
  salary DECIMAL(10,2) NOT NULL DEFAULT 0.0,
  status VARCHAR(20) NOT NULL
);

INSERT INTO users (email, salary, status)
VALUES ('deep1@gmail.com', 55000.00, 'ACTIVE'),
       ('bob@gmail.com', 45000.00, 'ACTIVE'),
       ('deep@gmail.com', 48000.00, 'ACTIVE'),
       ('deep@gmail.com', 53000.00, 'ACTIVE'),
       ('deep@gmail.com', 54000.00, 'ACTIVE'),
       ('deep@gmail.com', 51000.00, 'ACTIVE'),
       ('deep@gmail.com', 49000.00, 'ACTIVE'),
       ('deep@gmail.com', 52000.00, 'ACTIVE');

