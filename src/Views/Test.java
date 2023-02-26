package Views;

import Controllers.InMemoryTaskManager;
import Controllers.Managers;
import Models.EpicTask;
import Models.Status;
import Models.SubTask;
import Models.Task;

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
        InMemoryTaskManager taskManager = (InMemoryTaskManager) Managers.getDefaultTaskManager();
        // Создание задач
        taskManager.createTask(new Task("Таск 1", "Описание Таск 1"));
        taskManager.createTask(new Task("Таск 2", "Описание Таск 2"));
        taskManager.createEpicTask(new EpicTask("Эпик 1", "Описание Эпик 1"));
        taskManager.createEpicTask(new EpicTask("Эпик 1", "Описание Эпик 1"));
        taskManager.createSubTask(new SubTask("Сабтаск 1", "Описание сабтаск 1", 2));
        taskManager.createSubTask(new SubTask("Сабтаск 2", "Описание сабтаск 2", 2));
        taskManager.createSubTask(new SubTask("Сабтаск 3", "Описание сабтаск 3", 2));
        // Получение задач по id - 12 раз
        taskManager.getTaskById(0);
        taskManager.getTaskById(3);
        taskManager.getTaskById(1);
        taskManager.getEpicTaskById(2);
        taskManager.getTaskById(1);
        taskManager.getSubTaskById(4);
        taskManager.getEpicTaskById(3);
        taskManager.getSubTaskById(5);
        taskManager.getTaskById(1);
        taskManager.getSubTaskById(4);
        taskManager.getEpicTaskById(3);
        taskManager.getSubTaskById(4);
        // Проверка записи в историю просмотров задач не более 10 записей
        System.out.println("taskManager.getHistory() = " + taskManager.getHistory());
    }
}