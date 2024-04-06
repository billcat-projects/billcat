DROP TABLE IF EXISTS product;
CREATE TABLE product
(
    id          bigserial      not null primary key,
    name        varchar(50)    not null,
    category    varchar(50)    not null,
    brand       varchar(50)    not null,
    image       varchar(100)   null,
    description varchar(200)   null,
    rating      numeric(3, 1)  null default null,
    price       decimal(10, 2) not null
);
ALTER SEQUENCE product_id_seq RESTART WITH 1001;
COMMENT ON TABLE product is 'product';
COMMENT ON COLUMN product.name IS 'product name';
COMMENT ON COLUMN product.category IS 'category';
COMMENT ON COLUMN product.brand IS 'brand';
COMMENT ON COLUMN product.image IS 'image';
COMMENT ON COLUMN product.description IS 'description';
COMMENT ON COLUMN product.rating IS 'rating';
COMMENT ON COLUMN product.description IS 'description';
COMMENT ON COLUMN product.price IS 'price';