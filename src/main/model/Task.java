package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    protected int id;
    protected String name;
    protected String description;
    protected Status status;
    protected LocalDateTime startTime;
    protected Duration duration;
    protected final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public Task(String name, String description, String startTime, long duration) {
        this.name = name;
        this.description = description;
        this.startTime = setStartTime(startTime);
        setDuration(duration);
    }

    public DateTimeFormatter getFORMAT() {
        return FORMAT;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(long duration) {
        this.duration = Duration.ofMinutes(duration);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Type getType() {
        return Type.TASK;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public String getStartTimeToString() {
        String start = "null";
        if (!(startTime == null)) {
            start = startTime.format(FORMAT);
        }
        return start;
    }

    public long getDuration() {
        return duration.toMinutes();
    }

    public String getEndTimeToString() {
        String end = "null";
        if (!(startTime == null)) {
            end = startTime.plus(duration).format(FORMAT);
        }
        return end;
    }

    private LocalDateTime setStartTime(String startTime) {
        LocalDateTime start = null;
        if (!(Objects.equals(startTime, "null")) && !(Objects.equals(startTime, ""))) {
            start = LocalDateTime.parse(startTime, FORMAT);
        }
        return start;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}' + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && name.equals(task.name) && description.equals(task.description)
                && status == task.status && startTime == task.startTime && duration == task.duration;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status, startTime, duration);
    }
}
