SELECT store_address, total_quantity
FROM epicenter.product_types_by_store
WHERE product_type = 'Туризм'
ORDER BY total_quantity DESC LIMIT 1;

