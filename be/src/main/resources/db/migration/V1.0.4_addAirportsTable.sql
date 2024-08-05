create table airport
(
    id          bigint auto_increment
        primary key,
    iata_code        varchar(3) not null,
    name varchar(512) null
);