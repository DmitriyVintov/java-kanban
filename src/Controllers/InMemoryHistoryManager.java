package Controllers;

import Models.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс реализует интерфейс менеджера истории просмотров
 */
public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> history = new ArrayList<>();

    @Override
    public void addInHistory(Task task) {
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}