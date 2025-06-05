import java.sql.*;

public abstract class User {
    protected int userID;
    protected String name, email, passwd;

    public abstract void DashBoard();

    public static User signup(String name, String email, String passwd) {
        String checkQuery = "SELECT user_id FROM Users WHERE email = ?";
        String insertQuery = "INSERT INTO Users (name, email, password, role) VALUES (?, ?, ?, ?)";
    
        try (Connection conn = DBC.Connect()) {
            
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, email);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    System.out.println("Email already in use.");
                    return null;
                }
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                insertStmt.setString(1, name);
                insertStmt.setString(2, email);
                insertStmt.setString(3, passwd);
                insertStmt.setString(4, "CUSTOMER");

                int rowsAffected = insertStmt.executeUpdate();
                if (rowsAffected > 0) {
                    ResultSet generatedKeys = insertStmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int newUserId = generatedKeys.getInt(1);
                        return new Customer(newUserId, name, email, passwd);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
    }
    return null;
    }
    
    public static User login(String email, String passwd) {
        String query = "SELECT * FROM Users WHERE email = ? AND password = ?";
        try (Connection conn = DBC.Connect(); PreparedStatement stmt = conn.prepareStatement(query);){
            stmt.setString(1, email);
            stmt.setString(2, passwd);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("user_id");
                String name = rs.getString("name");
                String role = rs.getString("role");

                if ("ADMIN".equals(role)) {
                    return new Admin(id, name, email, passwd);
                } else if ("CUSTOMER".equals(role)) {
                    return new Customer(id, name, email, passwd);
                }
            }
            else{
                System.out.println("Check credentials and try again");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void listAllMovies() {
        String sql = "SELECT * FROM Movies";
        try (Connection conn = DBC.Connect(); 
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
            System.out.println("---- Movie List ----");
            System.out.printf("%-5s %-25s %-15s %-10s %-10s%n", "ID", "Title", "Genre", "Duration", "Language");
            System.out.println("---------------------------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("movie_id");
                String title = rs.getString("title");
                String genre = rs.getString("genre");
                int duration = rs.getInt("duration");
                String language = rs.getString("language");

                System.out.printf("%-5d %-25s %-15s %-10d %-10s%n", id, title, genre, duration, language);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void listAllSnacks() {
        String sql = "SELECT * FROM Snacks";
        try (Connection conn = DBC.Connect(); 
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            System.out.println("---- Snack Inventory ----");
            System.out.printf("%-5s %-20s %-10s %-10s%n", "ID", "Name", "Price(₹)", "Stock");
            System.out.println("--------------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("snack_id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int stock = rs.getInt("stock");

                System.out.printf("%-5d %-20s %-10.2f %-10d%n", id, name, price, stock);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
