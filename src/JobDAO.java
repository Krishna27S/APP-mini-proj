import java.sql.Connection;//Imports the Connection interface, which represents a connection to a database.
import java.sql.PreparedStatement;//Imports the PreparedStatement interface, which is used for executing precompiled SQL statements with or without input parameters.
import java.sql.ResultSet;//Imports the ResultSet interface, which provides methods for retrieving data returned by a database query.
import java.sql.SQLException;//Imports the SQLException class, which handles database access errors and other errors related to SQL operations.
import java.sql.Statement;//Imports the Statement interface, which is used for executing SQL queries against the database.
import java.util.ArrayList;//Imports the ArrayList class, which provides a resizable array implementation of the List interface.
import java.util.List;//Imports the List interface, which defines a collection of ordered elements that can contain duplicates.

import javax.swing.JOptionPane;
// Add this as first line in all .java files

public class JobDAO {

    public void createTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS jobs (
                id INT AUTO_INCREMENT PRIMARY KEY,
                title VARCHAR(200) NOT NULL,
                description TEXT,
                details TEXT,
                status VARCHAR(50) DEFAULT 'Active',
                created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                Statement stmt = conn.createStatement();
                stmt.execute(sql);
            } else {
                throw new SQLException("Failed to establish database connection");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Error creating table: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public List<Job> getAllJobs() {
        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT * FROM jobs ORDER BY created_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                
                while (rs.next()) {
                    Job job = new Job(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("details"),
                        rs.getString("status"),
                        rs.getTimestamp("created_date")
                    );
                    jobs.add(job);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Error fetching jobs: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
        return jobs;
    }

    public boolean addJob(String title, String description, String details) {
        String sql = "INSERT INTO jobs (title, description, details) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, title);
                pstmt.setString(2, description);
                pstmt.setString(3, details);
                
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Error adding job: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE jobs SET status = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, status);
                pstmt.setInt(2, id);
                
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Error updating status: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    public boolean deleteJob(int id) {
        String sql = "DELETE FROM jobs WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);
                
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Error deleting job: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
}