package controllers;

import models.Task;

import java.util.List;

/**
 * Интерфейс описывает методы добавления и получения истории просмотров задач
 */
public interface HistoryManager<T extends Task> {
    /**
     * Добавляет полученные задачи в историю просмотров
     *
     * @param t Все виды задач
     */
    void add(T t);

    /**
     * Получение списка истории просмотров задач
     */
    List<T> getHistory();

    /**
     * Удаление задачи из истории просмотров
     *
     * @param id
     */
    void remove(int id);
}