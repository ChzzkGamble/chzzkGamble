DROP TABLE IF EXISTS roulette_element CASCADE;
DROP TABLE IF EXISTS roulette CASCADE;

CREATE TABLE roulette(
    id          VARCHAR PRIMARY KEY,
    channel_id  VARCHAR NOT NULL
);

CREATE TABLE roulette_element(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR NOT NULL,
    count       INTEGER,
    roulette_id VARCHAR NOT NULL,
    FOREIGN KEY (roulette_id) REFERENCES roulette (id)
);