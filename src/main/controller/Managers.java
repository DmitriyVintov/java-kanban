package controller;

import model.Task;

/**
 * Утилитарный класс для создания менеджеров задач и истории просмотров
 */
public class Managers {
    public static String getPath() {
        return path;
    }

    private static final String path = "src/main/storage/save.csv";

    private Managers() {
    }

    /**
     * Статический метод создания менеджера задач по умолчанию
     *
     * @return InMemoryTaskManager
     */
    public static TaskManager getDefaultTaskManager() {
        return new InMemoryTaskManager();
    }

    public static TaskManager getFileBackedTaskManager() {
        return new FileBackedTaskManager(path);
    }

    /**
     * Статический метод создания менеджера истории просмотров задач по умолчанию
     *
     * @return InMemoryHistoryManager
     */
    public static HistoryManager<Task> getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}