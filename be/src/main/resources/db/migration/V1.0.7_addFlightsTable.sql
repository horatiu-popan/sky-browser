create table airline
(
    id          bigint auto_increment
        primary key,
    airline_id bigint not null,
    number varchar(4) not null,
    origin_id bigint not null,
    destination_id bigint not null,
    departure_time datetime not null,
    arrival_time datetime not null,
    foreign key (airline_id) references airline(id),
    foreign key (origin_id) references airport(id),
    foreign key (destination_id) references airport(id)
);