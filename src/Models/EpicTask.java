package Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EpicTask extends Task {
    private final List<Integer> idSubTasks;

    public EpicTask(String name, String description) {
        super(name, description);
        idSubTasks = new ArrayList<>();
    }

    public List<Integer> getIdSubTasks() {
        return idSubTasks;
    }

    public void setIdSubTasks(Integer idSubTasks) {
        this.idSubTasks.add(idSubTasks);
    }

    @Override
    public String toString() {
        return "Models.EpicTask{" +
                "idSubTasks=" + idSubTasks +
                ", id=" + getId() +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + getStatus() +
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
