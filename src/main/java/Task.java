import java.sql.Timestamp;
import java.time.LocalDate;

public class Task {
    private int id;
    private String title;
    private LocalDate dueDate;
    private boolean status;
    private Timestamp createdAt;

    Task(int id, String title, LocalDate dueDate, boolean status, Timestamp createdAt) {
        this.id = id;
        this.title = title;
        this.dueDate = dueDate;
        this.status = status;
        this.createdAt = createdAt;
    }

    int getId() {
        return id;
    }

    private void expendTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        while (title.length() < 30)
            expendTitle(title + " ");
        return  " id= " + id +
                "\t| title= '" + title + '\'' +
                "\t| dueDate= " + dueDate.toString() +
                "\t| status= " + status +
                "\t| createdAt= " + createdAt.toString() +
                " ";
    }
}
