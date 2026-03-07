create table menu_sku
(
    id           bigint auto_increment
        primary key,
    is_available bit                       not null,
    price        double                    not null,
    size         enum ('Large', 'Regular') not null,
    stock        int                       not null,
    menu_id      bigint                    null,
    constraint FKchetvl74h8evnny3vy8fd9j36
        foreign key (menu_id) references menu (id),
    check (`stock` >= 0)
);


