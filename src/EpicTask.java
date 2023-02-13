import java.util.HashMap;

public class EpicTask extends Task {
    public HashMap<Integer, SubTask> listSubTasks;

    public EpicTask(String name, String description) {
        super(name, description);
        super.type = "epictask";
        this.listSubTasks = new HashMap<>();
    }

    @Override
    public String toString() {
        return "EpicTask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", " + "\n" + "listSubTasks=" + listSubTasks +
                '}' + '\n';
    }
}
