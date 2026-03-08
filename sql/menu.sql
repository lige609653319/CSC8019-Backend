create table menu
(
    id       bigint auto_increment
        primary key,
    category varchar(50)  not null,
    name     varchar(255) not null,
    store_id bigint       not null,
    constraint unique_store_menu_name
        unique (store_id, name),
    constraint FK4sgenfcmk1jajhgctnkpn5erg
        foreign key (store_id) references store (id)
);


