create table menu
(
    id            bigint auto_increment
        primary key,
    category      enum ('CHOCOLATE', 'COFFEE', 'WATER') not null,
    is_available  bit                                   not null,
    large_price   double                                null,
    name          varchar(255)                          not null,
    regular_price double                                null,
    stock         int                                   not null,
    check (`stock` >= 0)
);


