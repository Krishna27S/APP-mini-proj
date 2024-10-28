import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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