import java.sql.*;

public class Admin extends User{
    public Admin(int id, String name, String email, String password) {
        this.userID = id;
        this.name = name;
        this.email = email;
        this.passwd = password;
    }

    @Override
    public void DashBoard() {
        System.out.println("Welcome, Admin " + name);
        // You can add admin-specific menu options here later
    }

    public void addMovie(String title, String genre, int duration, String language) {
        String sql = "INSERT INTO Movies (title, genre, duration, language) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBC.Connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, genre);
            stmt.setInt(3, duration);
            stmt.setString(4, language);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                    System.out.println("Movie added");
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editMovie(int MovieID, String newTitle, String newGenre, int newDuration, String newLanguage) {
        String sql = "UPDATE Movies SET title = ?, genre = ?, duration = ?, language = ? WHERE movie_id = ?";
        try (Connection conn = DBC.Connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newTitle);
            stmt.setString(2, newGenre);
            stmt.setInt(3, newDuration);
            stmt.setString(4, newLanguage);
            stmt.setInt(5, MovieID);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                    System.out.println("Movie edited");
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void removeMovie(int movieId){
        String sql = "DELETE FROM Movies WHERE movie_id = ?";
        try (Connection conn = DBC.Connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, movieId);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0){
                System.out.println("Removed movie");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Sry bruh, no can do");
        }
    }
    public void addSnack(String name, double price, int quantity) {
        String sql = "INSERT INTO Snacks (name, price, stock_quantity) VALUES (?, ?, ?)";
        try (Connection conn = DBC.Connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setDouble(2, price);
            stmt.setInt(3, quantity);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Snack addded");
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editSnack(int SnackID, String newName, double newPrice) {
        String sql = "UPDATE Snacks SET name = ?, price = ? WHERE snack_id = ?";
        try (Connection conn = DBC.Connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newName);
            stmt.setDouble(2, newPrice);
            stmt.setInt(3, SnackID);
            if (stmt.executeUpdate() > 0){
                System.out.println("Snack edited");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void removeSnack(int snackId) {
        String sql = "DELETE FROM Snacks WHERE snack_id = ?";
        try (Connection conn = DBC.Connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, snackId);
            int affectedRows = stmt.executeUpdate();
            if (stmt.executeUpdate() > 0){
                System.out.println("Removed snack");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void generateSalesReport() {
        String sql = """
            SELECT b.booking_id, b.user_id, s.seat_id, sn.name AS snack_name, bs.quantity, sn.price
            FROM Bookings b
            LEFT JOIN BookingSeats bs ON b.booking_id = bs.booking_id
            LEFT JOIN BookingSnacks bs2 ON b.booking_id = bs2.booking_id
            LEFT JOIN Snacks sn ON bs2.snack_id = sn.snack_id
            WHERE DATE(b.booking_time) = CURRENT_DATE
        """;
        try (Connection conn = DBC.Connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.printf("BookingID: %d, UserID: %d, Seat: %s, Snack: %s, Qty: %d, Price: %.2f%n",
                        rs.getInt("booking_id"),
                        rs.getInt("user_id"),
                        rs.getString("seat_id"),
                        rs.getString("snack_name"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}