import java.sql.*;

public class Customer extends User {
    public Customer(int id, String name, String email, String password) {
        this.userID = id;
        this.name = name;
        this.email = email;
        this.passwd = password;
    }

    @Override
    public void DashBoard() {
        System.out.println("Customer dashboard for " + name);
    }
    
    public void viewTimeSlotsForMovie(int movieId) {
        String sql = "SELECT * FROM TimeSlots WHERE movie_id = ?";
        try (Connection conn = DBC.Connect(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, movieId);
            ResultSet rs = stmt.executeQuery();

            System.out.printf("%-5s %-10s %-10s%n", "ID", "Screen", "Time");
            while (rs.next()) {
                System.out.printf("%-5d %-10d %-10s%n",
                        rs.getInt("slot_id"),
                        rs.getInt("screen_id"),
                        rs.getString("start_time"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewAvailableSeats(int slotId) {
        String sql = "SELECT * FROM Seats WHERE slot_id = ? AND is_booked = 0";
        try (Connection conn = DBC.Connect(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, slotId);
            ResultSet rs = stmt.executeQuery();

            System.out.println("Available Seats:");
            while (rs.next()) {
                System.out.println("Seat ID: " + rs.getInt("seat_id") + 
                                   " | Row: " + rs.getString("seat_row") + 
                                   " | Number: " + rs.getInt("seat_number"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//need function to add update and dlt timeslots
//add the code for filling seats in sql and the admin login too

}
