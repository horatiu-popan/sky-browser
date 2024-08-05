create table airline
(
    id          bigint auto_increment
        primary key,
    iata_code varchar(2) null,
    name varchar(512) null
);