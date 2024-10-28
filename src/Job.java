

import java.sql.Timestamp;

public class Job {
    private int id;
    private String title;
    private String description;
    private String details;
    private String status;
    private Timestamp createdDate;

    public Job(int id, String title, String description, String details, String status, Timestamp createdDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.details = details;
        this.status = status;
        this.createdDate = createdDate;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedDate() { return createdDate; }
    public void setCreatedDate(Timestamp createdDate) { this.createdDate = createdDate; }
}