import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class DatabaseConnection {
    static {
        try {
            // Register JDBC driver explicitly
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "MySQL Driver Error: " + e.getMessage());
        }
    }

    private static final String URL = "jdbc:mysql://localhost:3306/job_manager";
    private static final String USER = "root";
    private static final String PASSWORD = "helloworld"; // Your MySQL password

    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            
            // Create database if it doesn't exist
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS job_manager");
                stmt.executeUpdate("USE job_manager");
            }
            
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Connection Error: " + e.getMessage() + "\nPlease check if MySQL is running.",
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}