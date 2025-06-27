# Movie Booking System 🎬

A Java-based desktop application for booking movie tickets, featuring user and admin panels, seat selection, scheduling, snack options, and integrated payment simulation. Built using Java Swing for the GUI and MySQL for backend data storage.

---

## 📁 Project Structure

.  
│README.md  
│setup.sql  
│  
└───MBS  
├───bin  
│DBC.class  
│  
├───lib  
│mysql-connector-j-9.3.0.jar  
│  
└───src  
Admin.java  
AdminMoviePanel.java  
AdminPanel.java  
AdminSnackPanel.java  
AdminTimeSlotPanel.java  
App.java  
BookingPanel.java  
Customer.java  
CustomerMoviePanel.java  
CustomerPanel.java  
CustomerTicketsPanel.java  
CustomerTimeSlotPanel.java  
DBC.java  
LoginPanel.java  
Movie.java  
MovieListPanel.java  
PassHash.java  
PaymentPanel.java  
Seat.java  
SeatPanel.java  
SignUpPanel.java    
Snack.java  
SnackListPanel.java  
SnackPanel.java  
Ticket.java  
TimeSlot.java  
TimeSlotListPanel.java  
User.java  
UserPanel.java  

---

## 💻 Features

- **Admin panel** to manage movies, time slots, snacks, and view bookings  
- **Customer panel** for browsing, selecting movies, snacks, and confirming tickets  
- **Secure login and sign-up** with password hashing  
- **MySQL backend** to store all user, movie, and booking data  
- **Modular code** separated by role and functionality

---

## 🧪 Technologies Used

- Java  
- Java Swing  
- MySQL  
- JDBC  
- VS Code

---

## 🛠️ Setup Instructions

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/movie-booking-system.git
   cd movie-booking-system
2. **Create a MySQL database** by executing the setup.sql file in your MySQL environment.
3. **Run the app** by launching App.java in your IDE or compiling it via terminal.

## 📦 Dependencies

- **Java JDK** – Required to compile and run the application  
- **MySQL Server** – For storing and retrieving movie booking data  
- **MySQL JDBC Driver** – `mysql-connector-j-9.3.0.jar` included in the `lib/` folder  
- **VS Code / Any Java IDE** – For editing and running the source code
