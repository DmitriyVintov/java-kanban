public class SubTask extends Task {
    protected int idEpicTask;

    public SubTask(String name, String description, int idEpicTask) {
        super(name, description);
        super.type = "subtask";
        this.idEpicTask = idEpicTask;
    }

    @Override
    public String toString() {
        return '\n' + "SubTask{" +
                "idEpicTask=" + idEpicTask +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
