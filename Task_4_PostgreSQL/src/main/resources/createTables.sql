CREATE TABLE categories
(
    "id"   SERIAL PRIMARY KEY,
    "name" VARCHAR(30) NOT NULL
);

CREATE TABLE stores
(
    "id"       SERIAL PRIMARY KEY,
    "name"     VARCHAR(20) NOT NULL,
    "location" VARCHAR(30)
);

CREATE TABLE products
(
    "id"          SERIAL PRIMARY KEY,
    "name"        VARCHAR(35)    NOT NULL,
    "category_id" INTEGER REFERENCES categories ("id"),
    "price"       NUMERIC(10, 2) NOT NULL
);

CREATE TABLE store_products
(
    "id"         SERIAL PRIMARY KEY,
    "store_id"   INTEGER REFERENCES stores ("id"),
    "product_id" INTEGER REFERENCES products ("id"),
    "quantity"   INTEGER NOT NULL
);
