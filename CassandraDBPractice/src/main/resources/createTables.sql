create table if not exists epicenter.categories // 100
(
    id   UUID PRIMARY KEY,
    name varchar
);

create table if not exists epicenter.stores // 1000
(
    id        UUID PRIMARY KEY,
    name      varchar,
    "address" varchar
);

create table if not exists epicenter.store_products // 1m
(
    product_id   UUID,
    store_id     UUID,
    product_name varchar,
    product_type varchar,
    quantity     smallint,
    PRIMARY KEY (product_id, store_id)
);

create table if not exists epicenter.product_types_by_store // 2m
(
    product_type   varchar,
    store_id       UUID,
    store_address  varchar,
    total_quantity smallint,
    PRIMARY KEY (product_type, total_quantity, store_id)
) WITH CLUSTERING ORDER BY (total_quantity DESC, store_id ASC);