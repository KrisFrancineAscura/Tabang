import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/prescription_db";
        String username = "root"; // Replace with your MySQL username
        String password = ""; // Replace with your MySQL password
        return DriverManager.getConnection(url, username, password);
    }

    public static void main(String[] args) {
        try {
            Connection connection = getConnection();
            System.out.println("Connection established successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
