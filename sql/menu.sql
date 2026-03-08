create table menu
(
    id       bigint auto_increment
        primary key,
    category enum ('CHOCOLATE', 'COFFEE', 'WATER') not null,
    name     varchar(255)                          not null,
    store_id bigint                                not null,
    constraint FK4sgenfcmk1jajhgctnkpn5erg
        foreign key (store_id) references store (id)
);


