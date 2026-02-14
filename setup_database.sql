-- SkyHigh Airlines Database Setup Script
-- Run this in MySQL to create a fresh database

-- Drop and recreate database
DROP DATABASE IF EXISTS skyhigh;
CREATE DATABASE skyhigh;
USE skyhigh;

-- Create users table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER'
);

-- Create flights table
CREATE TABLE flights (
    flight_id VARCHAR(20) PRIMARY KEY,
    source VARCHAR(50) NOT NULL,
    destination VARCHAR(50) NOT NULL,
    departure_time VARCHAR(20),
    arrival_time VARCHAR(20),
    price DOUBLE,
    seats_available INT,
    economy_price DOUBLE,
    business_price DOUBLE,
    economy_seats INT,
    business_seats INT
);

-- Create bookings table
CREATE TABLE bookings (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    flight_id VARCHAR(20) NOT NULL,
    seat_class VARCHAR(20),
    booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (flight_id) REFERENCES flights(flight_id)
);

-- Insert a test admin user
-- Email: admin@skyhigh.com, Password: admin123
INSERT INTO users (name, email, password, phone, role) 
VALUES ('Admin User', 'admin@skyhigh.com', 'admin123', '1234567890', 'ADMIN');

-- Insert some sample flights
INSERT INTO flights (flight_id, source, destination, departure_time, arrival_time, price, seats_available, economy_price, business_price, economy_seats, business_seats)
VALUES 
    ('SH001', 'Delhi', 'Mumbai', '08:00', '10:30', 5000, 150, 5000, 12000, 120, 30),
    ('SH002', 'Mumbai', 'Bangalore', '14:00', '16:00', 4500, 180, 4500, 11000, 150, 30),
    ('SH003', 'Delhi', 'Kolkata', '10:30', '12:30', 4000, 160, 4000, 10000, 130, 30);

-- Show all tables
SHOW TABLES;

-- Verify users table
SELECT * FROM users;

SELECT 'Database setup complete!' as Status;
