import java.sql.*;

public class DBC {
    private static final String URL = "jdbc:mysql://localhost:3306/MBS";
    private static final String USER = "root";
    private static final String PASSWORD = "passwd";

    public static Connection Connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
