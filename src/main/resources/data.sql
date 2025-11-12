-- Insert test data for Car table
INSERT INTO Car (make, model, year, price, mileage, status) VALUES
-- Toyota vehicles
('Toyota', 'Corolla', 2023, 22500, 5000, 'available'),
('Toyota', 'Corolla', 2022, 21000, 15000, 'available'),
('Toyota', 'Camry', 2023, 28000, 3000, 'available'),
('Toyota', 'Camry', 2021, 24000, 35000, 'sold'),
('Toyota', 'RAV4', 2023, 32000, 2000, 'available'),
('Toyota', 'Highlander', 2022, 38000, 20000, 'available'),

-- Honda vehicles
('Honda', 'Civic', 2023, 25000, 4000, 'available'),
('Honda', 'Civic', 2022, 23000, 18000, 'available'),
('Honda', 'Accord', 2023, 30000, 2000, 'available'),
('Honda', 'Accord', 2021, 27000, 28000, 'sold'),
('Honda', 'CR-V', 2023, 35000, 1000, 'available'),
('Honda', 'Pilot', 2022, 42000, 25000, 'reserved'),

-- Ford vehicles
('Ford', 'Mustang', 2023, 45000, 1000, 'available'),
('Ford', 'Mustang', 2020, 35000, 40000, 'sold'),
('Ford', 'F-150', 2023, 55000, 500, 'available'),
('Ford', 'F-150', 2021, 48000, 30000, 'available'),
('Ford', 'Escape', 2023, 28000, 3000, 'available'),
('Ford', 'Explorer', 2022, 40000, 22000, 'available'),

-- BMW vehicles
('BMW', '3 Series', 2023, 42000, 2000, 'available'),
('BMW', '3 Series', 2021, 38000, 32000, 'sold'),
('BMW', '5 Series', 2023, 58000, 1000, 'available'),
('BMW', 'X5', 2022, 65000, 18000, 'available'),

-- Mercedes-Benz vehicles
('Mercedes-Benz', 'C-Class', 2023, 48000, 1500, 'available'),
('Mercedes-Benz', 'E-Class', 2022, 62000, 15000, 'available'),
('Mercedes-Benz', 'GLC', 2023, 52000, 2000, 'available'),

-- Chevrolet vehicles
('Chevrolet', 'Malibu', 2023, 26000, 3000, 'available'),
('Chevrolet', 'Silverado', 2023, 50000, 1000, 'available'),
('Chevrolet', 'Equinox', 2022, 29000, 20000, 'available'),

-- Hyundai vehicles
('Hyundai', 'Elantra', 2023, 20000, 2000, 'available'),
('Hyundai', 'Sonata', 2023, 25000, 1500, 'available'),
('Hyundai', 'Santa Fe', 2022, 32000, 18000, 'available'),

-- Kia vehicles
('Kia', 'Forte', 2023, 21000, 2500, 'available'),
('Kia', 'Optima', 2022, 26000, 22000, 'available'),
('Kia', 'Sorento', 2023, 33000, 3000, 'available');

-- Insert test data for Customer table
INSERT INTO Customer (name, email, phone, city) VALUES
('John Doe', 'john.doe@email.com', '555-0101', 'New York'),
('Jane Smith', 'jane.smith@email.com', '555-0102', 'Los Angeles'),
('Michael Johnson', 'michael.j@email.com', '555-0103', 'Chicago'),
('Sarah Williams', 'sarah.w@email.com', '555-0104', 'Houston'),
('Robert Brown', 'robert.b@email.com', '555-0105', 'Phoenix'),
('Emily Davis', 'emily.d@email.com', '555-0106', 'Philadelphia'),
('David Miller', 'david.m@email.com', '555-0107', 'San Antonio'),
('Lisa Wilson', 'lisa.w@email.com', '555-0108', 'San Diego'),
('James Moore', 'james.m@email.com', '555-0109', 'Dallas'),
('Jennifer Taylor', 'jennifer.t@email.com', '555-0110', 'San Jose'),
('Christopher Anderson', 'chris.a@email.com', '555-0111', 'Austin'),
('Mary Thomas', 'mary.t@email.com', '555-0112', 'Jacksonville'),
('Daniel Jackson', 'daniel.j@email.com', '555-0113', 'Fort Worth'),
('Patricia White', 'patricia.w@email.com', '555-0114', 'Columbus'),
('Matthew Harris', 'matthew.h@email.com', '555-0115', 'Charlotte'),
('Linda Martin', 'linda.m@email.com', '555-0116', 'San Francisco'),
('Mark Thompson', 'mark.t@email.com', '555-0117', 'Indianapolis'),
('Barbara Garcia', 'barbara.g@email.com', '555-0118', 'Austin'),
('Donald Martinez', 'donald.m@email.com', '555-0119', 'Seattle'),
('Susan Robinson', 'susan.r@email.com', '555-0120', 'Denver');

-- Insert test data for Sale table
INSERT INTO Sale (car_id, customer_id, sale_date, sale_price) VALUES
-- Sales from 2024
(4, 1, '2024-01-15', 23500),
(9, 2, '2024-02-20', 26500),
(14, 3, '2024-03-10', 34500),
(20, 4, '2024-04-05', 37500),
(19, 5, '2024-05-12', 39000),
(2, 6, '2024-06-18', 20500),
(8, 7, '2024-07-22', 22500),
(13, 8, '2024-08-14', 47000),
(21, 9, '2024-09-25', 60000),
(25, 10, '2024-10-30', 50500),

-- Sales from 2023
(4, 11, '2023-11-05', 23000),
(9, 12, '2023-10-20', 26000),
(14, 13, '2023-09-15', 34000),
(20, 14, '2023-08-10', 37000),
(19, 15, '2023-07-25', 38500),
(2, 16, '2023-06-30', 20000),
(8, 17, '2023-05-18', 22000),
(13, 18, '2023-04-12', 46500),
(21, 19, '2023-03-08', 59500),
(25, 20, '2023-02-14', 50000);
