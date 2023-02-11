import java.util.ArrayList;

public class EpicTask extends Task {
    public ArrayList<SubTask> listSubTasks;

    public EpicTask(String name, String description) {
        super(name, description);
        super.type = "epictask";
        this.listSubTasks = new ArrayList<>();
    }

    @Override
    public String toString() {
        return '\n' + "EpicTask{" +
                "listSubTasks=" + listSubTasks +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
