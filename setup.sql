CREATE DATABASE MBS;
USE MBS;

CREATE TABLE Users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'CUSTOMER') NOT NULL
);

CREATE TABLE Movies (
    movie_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    genre VARCHAR(50),
    duration INT,
    lang varchar(20)
);

CREATE TABLE TimeSlots (
    slot_id INT PRIMARY KEY AUTO_INCREMENT,
    movie_id INT NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    price DECIMAL(5, 2) NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES Movies(movie_id)
);

CREATE TABLE Seats (
    seat_id INT PRIMARY KEY AUTO_INCREMENT,
    slot_id INT NOT NULL,
    seat_number VARCHAR(3) NOT NULL,
    is_booked BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (slot_id) REFERENCES TimeSlots(slot_id)
);

CREATE TABLE Snacks (
    snack_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(5, 2) NOT NULL,
    quantity INT NOT NULL
);

CREATE TABLE Bookings (
    booking_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT NOT NULL,
    slot_id INT NOT NULL,
    booking_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    num_tickets INT NOT NULL,
    total_cost DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES Users(user_id),
    FOREIGN KEY (slot_id) REFERENCES TimeSlots(slot_id)
);

CREATE TABLE BookingSeats (
    booking_id INT NOT NULL,
    seat_id INT NOT NULL,
    PRIMARY KEY (booking_id, seat_id),
    FOREIGN KEY (booking_id) REFERENCES Bookings(booking_id),
    FOREIGN KEY (seat_id) REFERENCES Seats(seat_id)
);

CREATE TABLE BookingSnacks (
    booking_id INT NOT NULL,
    snack_id INT NOT NULL,
    quantity INT NOT NULL,
    PRIMARY KEY (booking_id, snack_id),
    FOREIGN KEY (booking_id) REFERENCES Bookings(booking_id),
    FOREIGN KEY (snack_id) REFERENCES Snacks(snack_id)
);


INSERT INTO USERS (name, email, password, role) VALUES ("admin", "admin@mbs.com", "passwd", "ADMIN");
