package models;

import java.util.Objects;

public class SubTask extends Task {
    private int idEpicTask;

    public SubTask(String name, String description, int idEpicTask) {
        super(name, description);
        this.idEpicTask = idEpicTask;
    }

    public int getIdEpicTask() {
        return idEpicTask;
    }

    public void setIdEpicTask(int idEpicTask) {
        this.idEpicTask = idEpicTask;
    }

    @Override
    public String toString() {
        return "models.SubTask{" + super.toString() +
                ", idEpicTask=" + idEpicTask +
                '}' + '\n';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return idEpicTask == subTask.idEpicTask;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idEpicTask);
    }
}
