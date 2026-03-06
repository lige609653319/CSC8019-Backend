create table menu
(
    id            bigint auto_increment
        primary key,
    category      enum ('CHOCOLATE', 'COFFEE', 'WATER') null,
    large_price   double                                null,
    name          varchar(255)                          null,
    regular_price double                                null,
    is_available  bit                                   null,
    stock         int                                   null
);


