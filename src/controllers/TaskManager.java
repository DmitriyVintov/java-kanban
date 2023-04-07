package controllers;

import models.EpicTask;
import models.Status;
import models.SubTask;
import models.Task;

import java.util.Map;

/**
 * Интерфейс описывает методы CRUD задач, Эпик задач и подзадач, а также метод добавления в историю просмотров
 * данных задач
 * Создание задач. Сам объект должен передаваться в качестве параметра;
 * Получение списка всех задач;
 * Получение по идентификатору;
 * Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра;
 * Удаление по идентификатору;
 * Удаление всех задач;
 * Получение списка истории просмотров задач
 */
public interface TaskManager {
    /**
     * Создание задачи и добавление ее в список tasksRepo
     *
     * @param task Задача
     */
    int createTask(Task task);

    /**
     * Создание Epic задачи и добавление ее в список epicTasksRepo
     *
     * @param epicTask Epic задача
     */
    int createEpicTask(EpicTask epicTask);

    /**
     * Cоздание подзадачи и добавление ее в список subTasksRepo
     *
     * @param subTask Подзадача
     */
    int createSubTask(SubTask subTask);

    /**
     * Получение списка всех задач
     */
    Map<Integer, Task> getTasksRepo();

    /**
     * Получение списка всех Epic задач
     */
    Map<Integer, EpicTask> getEpicTasksRepo();

    /**
     * Получение списка всех подзадач
     */
    Map<Integer, SubTask> getSubTasksRepo();

    /**
     * Получение задачи по id. Запись в список истории просмотров задач.
     * В списке максимум 10 элементов. Если больше - стирается последний
     *
     * @param id id задачи, которую необходимо найти
     */
    Task getTaskById(int id);

    /**
     * Получение Эпик задачи по id. Запись в список истории просмотров задач.
     * В списке максимум 10 элементов. Если больше - стирается последний
     *
     * @param id id Epic задачи, которую необходимо найти
     */
    EpicTask getEpicTaskById(int id);

    /**
     * Получение подзадачи по id. Запись в список истории просмотров задач.
     * В списке максимум 10 элементов. Если больше - стирается последний
     *
     * @param id id подзадачи, которую необходимо найти
     */
    SubTask getSubTaskById(int id);

    /**
     * Обновление задачи
     *
     * @param task   Задача, которую нужно обновить
     * @param status Статус, который нужно установить задаче
     */
    void updateTask(Task task, Status status);

    /**
     * Обновление Epic задачи (без участия пользователя)
     *
     * @param epicTask Epic задача
     */
    void updateEpicTask(EpicTask epicTask);

    /**
     * Обновление подзадачи
     *
     * @param subTask Подзадача
     * @param status  Статус, который нужно установить подзадаче
     */
    void updateSubTask(SubTask subTask, Status status);

    /**
     * Удаление задачи по id
     *
     * @param id id задачи, которую нужно удалить
     */
    void deleteTaskById(int id);

    /**
     * Удаление Epic задачи
     *
     * @param id id Epic задачи, которую нужно удалить
     */
    void deleteEpicTaskById(int id);

    /**
     * Удаление подзадачи
     *
     * @param id id подзадачи, которую нужно удалить
     */
    void deleteSubTaskById(int id);

    /**
     * Удаление всех задач
     */
    void deleteAllTasks();

    /**
     * Удаление всех Epic задач
     */
    void deleteAllEpicTasks();

    /**
     * Удаление всех подзадач
     */
    void deleteAllSubTasks();
}