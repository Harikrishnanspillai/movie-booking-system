create database mbs;
use mbs;
create table users (
	UserID int,
    UserName varchar(255),
    Passwd varchar(255),
    UserRole varchar(10)
);
create table movies (
	MovieID int,
    MovieName varchar(255),
    TimeSlots varchar(255),
    Price float
);
create table snacks (
	SnackID int,
    SnackName varchar(255),
    Price float
);
create table report (
	TicketID int,
    Movie varchar(255),
    Snacks varchar(255),
    MoviePrice float,
    SnackPrice float,
    Total float
);