-- Clear all existing products to reload with new data
USE foodfactory;

-- Delete all orders first (foreign key constraint)
DELETE FROM orders;

-- Delete all cart items (foreign key constraint)
DELETE FROM cart_items;

-- Delete all products
DELETE FROM product_table;

-- Reset auto increment
ALTER TABLE product_table AUTO_INCREMENT = 1;

SELECT 'All products cleared successfully!' AS message;
