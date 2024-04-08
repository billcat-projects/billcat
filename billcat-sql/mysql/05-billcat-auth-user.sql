create table users
(
    id           bigint       not null auto_increment comment 'id',
    avatar       varchar(255) null comment 'avatar url',
    first_name   varchar(32)  null comment 'first name',
    last_name    varchar(16)  null comment 'last name',
    username     varchar(32)  not null unique comment 'username',
    email        varchar(32)  not null unique comment 'email',
    password     varchar(64)  not null comment 'password',
    birth_date   date         null comment 'birth date',
    phone_number varchar(16)  null comment 'phone number',
    status       tinyint      not null default 0 comment 'status:0(normal),1(banned),2(deleted)',
    role         varchar(20)  not null comment 'ADMIN,USER',
    created_at   timestamp    not null default current_timestamp comment 'created at',
    deleted_at   timestamp    null comment 'deleted at',
    primary key (id)
) auto_increment = 1000 comment 'User';
