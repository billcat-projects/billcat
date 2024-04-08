DROP TABLE IF EXISTS product;
CREATE TABLE product
(
    id          bigint         not null auto_increment comment 'ID',
    name        varchar(50)    not null comment 'product name',
    category    varchar(50)    not null comment 'category',
    brand       varchar(50)    not null comment 'brand',
    image       varchar(100)   null comment 'image',
    description varchar(200)   null comment 'description',
    rating      numeric(3, 1)  null default null comment 'rating',
    price       decimal(10, 2) not null comment 'price',
    primary key (id)
) auto_increment = 100 comment 'Product';
