CREATE INDEX category_id_idx ON products (category_id);

CREATE INDEX store_id_idx ON store_products (store_id);
CREATE INDEX product_id_idx ON store_products (product_id);

-- CREATE INDEX idx_store_products_store_id_product_id ON store_products(store_id, product_id);
