package controllers;

import models.Task;

import java.util.List;

/**
 * Интерфейс описывает методы добавления и получения истории просмотров задач
 */
public interface HistoryManager {
    /**
     * Добавляет полученные задачи в историю просмотров
     *
     * @param task Все виды задач
     */
    void add(Task task);

    /**
     * Получение списка истории просмотров задач
     */
    List<Task> getHistory();
}
