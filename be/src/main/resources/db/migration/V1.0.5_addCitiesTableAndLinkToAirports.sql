create table city
(
    id          bigint auto_increment
        primary key,
    name varchar(512) null
);

ALTER TABLE airport
    ADD COLUMN city_id BIGINT,
    ADD CONSTRAINT fk_airport_city
    FOREIGN KEY (city_id) REFERENCES city(id);