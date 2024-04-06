DELETE
FROM product;
INSERT INTO product(id, name, category, brand, image, description, rating, price)
values (1, 'iPhone 13', 'cellphone', 'Apple', '/images/product/phone1.jpg', 'A popular cellphone', 4.2, 6188),
       (2, 'iPhone 14 Pro', 'cellphone', 'Apple', '/images/product/phone2.jpg', 'A popular cellphone', 4.5, 9288),
       (3, 'Redmi K50', 'cellphone', 'Redmi', '/images/product/phone3.jpg', 'A popular cellphone', 4.5, 2388),
       (4, 'Free Shirt', 'shirts', 'Nike', '/images/product/shirt1.jpg', 'A popular shirt', 4.5, 70),
       (5, 'Fit Shirt', 'shirts', 'Adidas', '/images/product/shirt2.jpg', 'A popular shirt', 3.2, 80),
       (6, 'Slim Shirt', 'shirts', 'Raymond', '/images/product/shirt3.jpg', 'A popular shirt', 4.5, 90),
       (7, 'Golf Pants', 'pants', 'Oliver', '/images/product/pants1.jpg', 'Smart looking pants', 2.9, 90),
       (8, 'Fit Pants', 'pants', 'Zara', '/images/product/pants2.jpg', 'A popular pants', 3.5, 95),
       (9, 'Classic Pants', 'pants', 'Casely', '/images/product/pants3.jpg', 'A popular pants', 2.4, 75),
       (10, 'Spring in Action', 'book', 'Manning', '/images/product/book1.jpg', 'A popular book for backend developer',
        4.5, 75),
       (11, 'Fullstack React with TypeScript', 'book', 'Fullstack.io', '/images/product/book2.jpg', 'A frontend book',
        4.5, 102);