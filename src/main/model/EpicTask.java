package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EpicTask extends Task {
    private final List<Integer> idSubTasks;
    private LocalDateTime endTime;

    public EpicTask(String name, String description, String startTime, long duration) {
        super(name, description, startTime, duration);
        idSubTasks = new ArrayList<>();
        endTime = null;
    }

    @Override
    public void setDuration(long duration) {
        if (this.startTime == null) {
            this.duration = Duration.ofMinutes(0);
        } else {
            this.duration = Duration.ofMinutes(duration);
        }
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String getEndTimeToString() {
        String end = "null";
        if (!(endTime == null)) {
            end = endTime.format(getFORMAT());
        }
        return end;
    }

    @Override
    public void setStartTime(LocalDateTime startTime) {
        super.setStartTime(startTime);
    }

    public List<Integer> getIdSubTasks() {
        return idSubTasks;
    }

    public void setIdSubTasks(Integer idSubTasks) {
        this.idSubTasks.add(idSubTasks);
    }

    @Override
    public Type getType() {
        return Type.EPIC;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", idSubTasks=" + idSubTasks +
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
