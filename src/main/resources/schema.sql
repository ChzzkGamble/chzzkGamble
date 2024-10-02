DROP TABLE IF EXISTS roulette_element CASCADE;
DROP TABLE IF EXISTS roulette CASCADE;
DROP TABLE IF EXISTS advertise CASCADE;
DROP TABLE IF EXISTS chat CASCADE;

CREATE TABLE roulette(
    id           uuid default random_uuid() PRIMARY KEY,
    channel_name VARCHAR NOT NULL,
    voting       Boolean NOT NULL,
    created_at   TIMESTAMP NOT NULL
);

CREATE TABLE roulette_element(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR NOT NULL,
    count        INTEGER,
    roulette_id  VARCHAR NOT NULL,
    FOREIGN KEY (roulette_id) REFERENCES roulette (id)
);

CREATE TABLE advertise(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR NOT NULL,
    image_url    VARCHAR NOT NULL,
    cost         BIGINT NOT NULL,
    created_at   TIMESTAMP NOT NULL
);

CREATE TABLE chat(
      id           BIGINT AUTO_INCREMENT PRIMARY KEY,
      channel_name VARCHAR NOT NULL,
      opened       Boolean NOT NULL,
      created_at   TIMESTAMP NOT NULL
);