CREATE TABLE films (
                       id BIGSERIAL PRIMARY KEY,
                       film_id BIGINT UNIQUE NOT NULL,
                       film_name VARCHAR(255),
                       year INT,
                       rating DOUBLE PRECISION,
                       description TEXT
);