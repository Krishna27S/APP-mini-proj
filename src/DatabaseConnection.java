

import java.sql.*;
import javax.swing.JOptionPane;

public class DatabaseConnection {
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "MySQL Driver Error: " + e.getMessage());
        }
    }

    private static final String URL = "jdbc:mysql://localhost:3306/job_manager?createDatabaseIfNotExist=true";
    private static final String USER = "root";
    private static final String PASSWORD = "helloworld"; // Change this to your MySQL password

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Connection Error: " + e.getMessage() +
                            "\nPlease check if MySQL is running.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}