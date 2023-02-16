import java.util.ArrayList;
import java.util.Objects;

public class EpicTask extends Task {
    private final ArrayList<Integer> idSubTasks;

    public EpicTask(String name, String description) {
        super(name, description);
        idSubTasks = new ArrayList<>();
    }

    public ArrayList<Integer> getIdSubTasks() {
        return idSubTasks;
    }

    public void setIdSubTasks(Integer idSubTasks) {
        this.idSubTasks.add(idSubTasks);
    }

    @Override
    public String toString() {
        return "EpicTask{" +
                "idSubTasks=" + idSubTasks +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EpicTask epicTask = (EpicTask) o;
        return idSubTasks.equals(epicTask.idSubTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idSubTasks);
    }
}
