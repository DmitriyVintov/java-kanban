package controllers;

import models.Task;

/**
 * Утилитарный класс для создания менеджеров задач и истории просмотров
 */
public class Managers {
    private Managers(){}
    /**
     * Статический метод создания менеджера задач по умолчанию
     *
     * @return InMemoryTaskManager
     */
    public static TaskManager getDefaultTaskManager() {
        return new InMemoryTaskManager();
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