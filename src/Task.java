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

    public String getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return '\n' + "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description)
                && Objects.equals(status, task.status) && Objects.equals(type, task.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status, type);
    }
}
