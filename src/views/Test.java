package views;

import controllers.InMemoryHistoryManager;
import controllers.InMemoryTaskManager;
import controllers.Managers;
import models.EpicTask;
import models.Status;
import models.SubTask;
import models.Task;

/**
 * Утилитарный класс для тестирования приложения
 */
public final class Test {
    private Test() {
    }

    /**
     * Статический метод тестирования CRUD методов всех типов задач
     */
    public static void testCRUD() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        //Инициализация задач
        Task newT1 = new Task("Помыть полы", "До вечера помыть во всем доме");
        Task newT2 = new Task("Починить розетки", "На первом этаже");
        EpicTask newE1 = new EpicTask("Переезд", "Переезд в новый дом");
        SubTask newS1 = new SubTask("Вынести мусор", "Выкинуть старое пианино", 2);
        SubTask newS2 = new SubTask("Собрать коробки", "Положить одежду в коробки", 2);
        EpicTask newE2 = new EpicTask("Переезд", "Переезд в новый дом");
        SubTask newS3 = new SubTask("Вынести мусор", "Выкинуть старое пианино", 5);
        SubTask newS4 = new SubTask("Собрать коробки", "Положить одежду в коробки", 5);

        // Создание задач
        System.out.println("Создание задач");
        int taskN1 = inMemoryTaskManager.createTask(newT1);
        int taskN2 = inMemoryTaskManager.createTask(newT2);
        int epicN1 = inMemoryTaskManager.createEpicTask(newE1);
        int subN2 = inMemoryTaskManager.createSubTask(newS2);
        int epicN2 = inMemoryTaskManager.createEpicTask(newE2);

        // Проверка записи созданных задач
        System.out.println("\nmanagerTasks.getTasksRepo() = " + inMemoryTaskManager.getTasksRepo());
        System.out.println("managerTasks.getEpicTasksRepo() = " + inMemoryTaskManager.getEpicTasksRepo());
        System.out.println("managerTasks.getSubTasksRepo() = " + inMemoryTaskManager.getSubTasksRepo());

        // Проверка поиска задач по id
        System.out.println("\nПроверка поиска по id");
        System.out.println("managerTasks.getTaskById(taskN1) = " + inMemoryTaskManager.getTaskById(taskN1));
        System.out.println("managerTasks.getEpicTaskById(epicN1) = " + inMemoryTaskManager.getEpicTaskById(epicN1));
        System.out.println("managerTasks.getSubTaskById(subN2) = " + inMemoryTaskManager.getSubTaskById(subN2));

        // Изменение статусов
        System.out.println("\nИзменение статусов");
        inMemoryTaskManager.updateTask(newT2, Status.DONE);
        inMemoryTaskManager.updateSubTask(newS1, Status.IN_PROGRESS);
        inMemoryTaskManager.updateSubTask(newS2, Status.DONE);
        inMemoryTaskManager.updateSubTask(newS3, Status.DONE);
        inMemoryTaskManager.updateSubTask(newS4, Status.DONE);

        // Проверка изменения статусов задач
        System.out.println("\nПроверка изменения статусов");
        System.out.println("managerTasks.getTasksRepo() = " + inMemoryTaskManager.getTasksRepo());
        System.out.println("managerTasks.getEpicTasksRepo() = " + inMemoryTaskManager.getEpicTasksRepo());
        System.out.println("managerTasks.getSubTasksRepo() = " + inMemoryTaskManager.getSubTasksRepo());

        // Удаление задач по id
        System.out.println("\nУдаление по id");
        inMemoryTaskManager.deleteTaskById(taskN2);
        inMemoryTaskManager.deleteEpicTaskById(epicN2);
        inMemoryTaskManager.deleteSubTaskById(subN2);

        // Проверка удаления задач по id
        System.out.println("\nПроверка удаления по id");
        System.out.println("managerTasks.getTasksRepo() = " + inMemoryTaskManager.getTasksRepo());
        System.out.println("managerTasks.getEpicTasksRepo() = " + inMemoryTaskManager.getEpicTasksRepo());
        System.out.println("managerTasks.getSubTasksRepo() = " + inMemoryTaskManager.getSubTasksRepo());

        // Удаление всех задач
        System.out.println("\nУдаление всех задач");
        inMemoryTaskManager.deleteAllTasks();
        inMemoryTaskManager.deleteAllEpicTasks();
        inMemoryTaskManager.deleteAllSubTasks();

        // Проверка удаления всех задач
        System.out.println("\nПроверка удаления всех задач");
        System.out.println("managerTasks.getTasksRepo() = " + inMemoryTaskManager.getTasksRepo());
        System.out.println("managerTasks.getEpicTasksRepo() = " + inMemoryTaskManager.getEpicTasksRepo());
        System.out.println("managerTasks.getSubTasksRepo() = " + inMemoryTaskManager.getSubTasksRepo());
    }

    /**
     * Статический метод тестирования получения списка истории просмотров задач
     */
    public static void testGetHistory() {
        // Создание менеджера задач по умолчанию
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefaultTaskManager();
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
        // Создание задач
        Task task1 = new Task("Таск 1", "Описание Таск 1");
        inMemoryTaskManager.createTask(task1);
        Task task2 = new Task("Таск 2", "Описание Таск 2");
        inMemoryTaskManager.createTask(task2);
        EpicTask epicTask1 = new EpicTask("Эпик 1", "Описание Эпик 1");
        inMemoryTaskManager.createEpicTask(epicTask1);
        EpicTask epicTask2 = new EpicTask("Эпик 2", "Описание Эпик 2");
        inMemoryTaskManager.createEpicTask(epicTask2);
        SubTask subTask1 = new SubTask("Сабтаск 1", "Описание сабтаск 1", 2);
        inMemoryTaskManager.createSubTask(subTask1);
        SubTask subTask2 = new SubTask("Сабтаск 2", "Описание сабтаск 2", 2);
        inMemoryTaskManager.createSubTask(subTask2);
        SubTask subTask3 = new SubTask("Сабтаск 3", "Описание сабтаск 3", 2);
        inMemoryTaskManager.createSubTask(subTask3);
        // Получение задач по id
        inMemoryTaskManager.getTaskById(0);
        inMemoryTaskManager.getTaskById(3);
        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getEpicTaskById(2);
        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getSubTaskById(4);
        inMemoryTaskManager.getEpicTaskById(3);
        inMemoryTaskManager.getSubTaskById(5);
        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getSubTaskById(4);
        inMemoryTaskManager.getEpicTaskById(3);
        inMemoryTaskManager.getSubTaskById(4);
        // Проверка записи в историю просмотров задач
        System.out.println("taskManager.historyManager.getHistory() = " + inMemoryTaskManager.getHistory());
        // Добавление задач в список истории просмотров с помощью inMemoryHistoryManager
        inMemoryHistoryManager.add(task1);
        System.out.println("inMemoryHistoryManager.getHistory() = " + inMemoryHistoryManager.getHistory());
        inMemoryHistoryManager.add(subTask1);
        inMemoryHistoryManager.add(task2);
        inMemoryHistoryManager.add(epicTask1);
        inMemoryHistoryManager.add(subTask1);
        inMemoryHistoryManager.add(epicTask2);
        inMemoryHistoryManager.add(subTask3);
        inMemoryHistoryManager.add(task2);
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(subTask1);
        inMemoryHistoryManager.add(epicTask2);
        inMemoryHistoryManager.add(subTask2);
        // Проверка записи в историю просмотров задач
        System.out.println("inMemoryHistoryManager.getHistory() = " + inMemoryHistoryManager.getHistory());
        System.out.println("inMemoryHistoryManager.getHistory() = " + inMemoryHistoryManager.getHistory());
        // Удаление задач из истории просмотров
        inMemoryHistoryManager.remove(2);
        inMemoryHistoryManager.remove(5);
        inMemoryHistoryManager.remove(20);
        System.out.println("inMemoryHistoryManager.getHistory() = " + inMemoryHistoryManager.getHistory());
    }
}