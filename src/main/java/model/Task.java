
package model;
import java.time.LocalDate;

public class Task {
    private long id;
    private String owner;
    private String title;
    private String description;
    private LocalDate dueDate;
    private boolean completed;

    public Task() {}

    public Task(long id, String owner, String title, String description, LocalDate dueDate, boolean completed) {
        this.id = id;
        this.owner = owner;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.completed = completed;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
    

