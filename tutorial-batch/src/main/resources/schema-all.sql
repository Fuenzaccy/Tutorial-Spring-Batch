DROP TABLE category IF EXISTS;
DROP TABLE IF EXISTS game;

CREATE TABLE category  (
    category_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(20),
    type VARCHAR(20),
    characteristics VARCHAR(30)
);

CREATE TABLE games (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    age_recommended INT,
    stock INT
);

INSERT INTO games (title, age_recommended, stock) VALUES
('Fortnite', 12, 5),
('Pac-Man', 16, 0),
('Mario Bros', 8, 10);