-- Test Queries for Car Dealer NL2SQL Application
-- These queries demonstrate what the LLM should generate from natural language

-- Query 1: Show all available Toyota cars
SELECT * FROM Car WHERE make = 'Toyota' AND status = 'available';

-- Query 2: Show all cars under $25,000
SELECT * FROM Car WHERE price < 25000 ORDER BY price DESC;

-- Query 3: Find all Honda and Toyota vehicles
SELECT * FROM Car WHERE make IN ('Honda', 'Toyota') ORDER BY make, model;

-- Query 4: List cars with price between $30,000 and $50,000
SELECT make, model, year, price FROM Car WHERE price BETWEEN 30000 AND 50000 ORDER BY price;

-- Query 5: Show all sold cars
SELECT * FROM Car WHERE status = 'sold';

-- Query 6: Get the most expensive car
SELECT * FROM Car ORDER BY price DESC LIMIT 1;

-- Query 7: Get the cheapest available car
SELECT * FROM Car WHERE status = 'available' ORDER BY price ASC LIMIT 1;

-- Query 8: Count cars by make
SELECT make, COUNT(*) as count FROM Car GROUP BY make ORDER BY count DESC;

-- Query 9: Average price by make
SELECT make, AVG(price) as average_price FROM Car GROUP BY make ORDER BY average_price DESC;

-- Query 10: Cars with high mileage (over 30,000 miles)
SELECT make, model, year, mileage, price FROM Car WHERE mileage > 30000 ORDER BY mileage DESC;

-- Query 11: Get all customers from New York
SELECT * FROM Customer WHERE city = 'New York';

-- Query 12: List customers from California cities
SELECT * FROM Customer WHERE city IN ('Los Angeles', 'San Francisco', 'San Diego', 'San Jose');

-- Query 13: Count customers by city
SELECT city, COUNT(*) as customer_count FROM Customer GROUP BY city ORDER BY customer_count DESC;

-- Query 14: Show all sales with car and customer details
SELECT 
    s.id,
    s.sale_date,
    s.sale_price,
    c.make,
    c.model,
    c.year,
    cu.name,
    cu.city
FROM Sale s
JOIN Car c ON s.car_id = c.id
JOIN Customer cu ON s.customer_id = cu.id
ORDER BY s.sale_date DESC;

-- Query 15: Total sales revenue
SELECT SUM(sale_price) as total_revenue FROM Sale;

-- Query 16: Sales by month (2024)
SELECT 
    DATE_TRUNC('month', sale_date) as month,
    COUNT(*) as sales_count,
    SUM(sale_price) as total_revenue
FROM Sale
WHERE EXTRACT(YEAR FROM sale_date) = 2024
GROUP BY DATE_TRUNC('month', sale_date)
ORDER BY month DESC;

-- Query 17: Top selling makes
SELECT 
    c.make,
    COUNT(s.id) as sales_count,
    SUM(s.sale_price) as total_revenue
FROM Sale s
JOIN Car c ON s.car_id = c.id
GROUP BY c.make
ORDER BY sales_count DESC;

-- Query 18: Cars sold by customer John Doe
SELECT 
    c.make,
    c.model,
    c.year,
    s.sale_date,
    s.sale_price
FROM Sale s
JOIN Car c ON s.car_id = c.id
JOIN Customer cu ON s.customer_id = cu.id
WHERE cu.name = 'John Doe';

-- Query 19: Average sale price by make
SELECT 
    c.make,
    AVG(s.sale_price) as average_sale_price,
    COUNT(s.id) as sales_count
FROM Sale s
JOIN Car c ON s.car_id = c.id
GROUP BY c.make
ORDER BY average_sale_price DESC;

-- Query 20: Recent sales (last 30 days)
SELECT 
    s.id,
    s.sale_date,
    c.make,
    c.model,
    s.sale_price,
    cu.name
FROM Sale s
JOIN Car c ON s.car_id = c.id
JOIN Customer cu ON s.customer_id = cu.id
WHERE s.sale_date >= CURRENT_DATE - INTERVAL '30 days'
ORDER BY s.sale_date DESC;

-- Query 21: Available cars by year
SELECT year, COUNT(*) as count FROM Car WHERE status = 'available' GROUP BY year ORDER BY year DESC;

-- Query 22: Most popular car model
SELECT make, model, COUNT(*) as count FROM Car GROUP BY make, model ORDER BY count DESC LIMIT 1;

-- Query 23: Cars with low mileage (under 5,000 miles)
SELECT make, model, year, mileage, price FROM Car WHERE mileage < 5000 ORDER BY mileage ASC;

-- Query 24: Luxury cars (price over $50,000)
SELECT make, model, year, price FROM Car WHERE price > 50000 ORDER BY price DESC;

-- Query 25: Budget cars (under $25,000) sorted by price
SELECT make, model, year, price FROM Car WHERE price < 25000 ORDER BY price ASC;
