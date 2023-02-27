package controllers;

import models.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс реализует интерфейс менеджера истории просмотров
 */
public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (history.size() >= 10) {
            history.remove(0);
        }
        history.add(task.getId(), task);
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}