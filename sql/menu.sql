create table menu
(
    id       bigint auto_increment
        primary key,
    category enum ('CHOCOLATE', 'COFFEE', 'WATER') not null,
    name     varchar(255)                          not null
);


