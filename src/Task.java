import java.util.Objects;

public class Task {
    protected int id;
    protected String name;
    protected String description;
    protected String status;
    protected String type;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.id = 0;
        this.status = "NEW";
        this.type = "task";
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                '}' + '\n';
    }
}
