-- Create database (run this separately if needed)
-- CREATE DATABASE car_dealer;

-- Drop existing tables if they exist (for clean setup)
DROP TABLE IF EXISTS Sale CASCADE;
DROP TABLE IF EXISTS Car CASCADE;
DROP TABLE IF EXISTS Customer CASCADE;

-- Create Car table
CREATE TABLE Car (
    id SERIAL PRIMARY KEY,
    make VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    year INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    mileage INT NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('available', 'sold', 'reserved'))
);

-- Create Customer table
CREATE TABLE Customer (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    city VARCHAR(50)
);

-- Create Sale table
CREATE TABLE Sale (
    id SERIAL PRIMARY KEY,
    car_id INT NOT NULL REFERENCES Car(id),
    customer_id INT NOT NULL REFERENCES Customer(id),
    sale_date DATE NOT NULL,
    sale_price DECIMAL(10, 2) NOT NULL
);

-- Create indexes for better query performance
CREATE INDEX idx_car_make ON Car(make);
CREATE INDEX idx_car_price ON Car(price);
CREATE INDEX idx_car_year ON Car(year);
CREATE INDEX idx_car_status ON Car(status);
CREATE INDEX idx_customer_city ON Customer(city);
CREATE INDEX idx_sale_car_id ON Sale(car_id);
CREATE INDEX idx_sale_customer_id ON Sale(customer_id);
CREATE INDEX idx_sale_date ON Sale(sale_date);
