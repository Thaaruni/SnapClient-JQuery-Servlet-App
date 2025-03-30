CREATE TABLE customer
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    address     VARCHAR(250) NOT NULL,
    profile_pic VARCHAR(500) NOT NULL
);