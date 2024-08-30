CREATE TABLE roulette(
    id           uuid default random_uuid() PRIMARY KEY,
    channel_id   VARCHAR NOT NULL,
    channel_name VARCHAR NOT NULL,
    created_at   TIMESTAMP NOT NULL
);

CREATE TABLE roulette_element(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR NOT NULL,
    count        INTEGER,
    roulette_id  VARCHAR NOT NULL,
    FOREIGN KEY (roulette_id) REFERENCES roulette (id)
);