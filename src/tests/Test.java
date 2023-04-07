package tests;

import controllers.FileBackedTaskManager;
import controllers.InMemoryTaskManager;
import controllers.Managers;
import models.EpicTask;
import models.Status;
import models.SubTask;
import models.Task;

import java.io.IOException;

/**
 * Утилитарный класс для тестирования приложения
 */
public final class Test {
    private Test() {
    }

    /**
     * Статический метод тестирования CRUD методов всех типов задач
     */
    public static void testTasksCRUD() {
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
     * Статический метод тестирования создания задач
     */
    public static void createTasks() {
        FileBackedTaskManager defaultTaskManager = (FileBackedTaskManager) Managers.getDefaultTaskManager();
        // Создание задач
        Task task1 = new Task("Таск 1", "Описание Таск 1");
        defaultTaskManager.createTask(task1);
        Task task2 = new Task("Таск 2", "Описание Таск 2");
        defaultTaskManager.createTask(task2);
        EpicTask epicTask1 = new EpicTask("Эпик 1", "Описание Эпик 1");
        defaultTaskManager.createEpicTask(epicTask1);
        EpicTask epicTask2 = new EpicTask("Эпик 2", "Описание Эпик 2");
        defaultTaskManager.createEpicTask(epicTask2);
        SubTask subTask1 = new SubTask("Сабтаск 1", "Описание сабтаск 1", 2);
        defaultTaskManager.createSubTask(subTask1);
        SubTask subTask2 = new SubTask("Сабтаск 2", "Описание сабтаск 2", 2);
        defaultTaskManager.createSubTask(subTask2);
        SubTask subTask3 = new SubTask("Сабтаск 3", "Описание сабтаск 3", 2);
        defaultTaskManager.createSubTask(subTask3);
    }

    /**
     * Статический метод тестирования создания истории просмотров задач
     */
    public static void testSaveHistoryInMemory() {
        // Создание менеджера задач по умолчанию
        FileBackedTaskManager defaultTaskManager = (FileBackedTaskManager) Managers.getDefaultTaskManager();
        // Получение задач по id
        defaultTaskManager.getTaskById(0);
        defaultTaskManager.getTaskById(1);
        defaultTaskManager.getEpicTaskById(2);
        defaultTaskManager.getSubTaskById(4);
        defaultTaskManager.getEpicTaskById(3);
        defaultTaskManager.getSubTaskById(5);
        defaultTaskManager.getSubTaskById(4);
        defaultTaskManager.getEpicTaskById(3);
        defaultTaskManager.getSubTaskById(6);
    }

    /**
     * Статический метод тестирования сохранения списка задач и истории просмотров в файл
     */
    public static void testSaveOnFile() throws IOException {
        String fileSave = "src/storage/save.csv";
        // Создание менеджера задач по умолчанию
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(fileSave);
        Task task1 = new Task("Таск 1", "Описание Таск 1");
        fileBackedTaskManager.createTask(task1);
        Task task2 = new Task("Таск 2", "Описание Таск 2");
        fileBackedTaskManager.createTask(task2);
        EpicTask epicTask1 = new EpicTask("Эпик 1", "Описание Эпик 1");
        fileBackedTaskManager.createEpicTask(epicTask1);
        EpicTask epicTask2 = new EpicTask("Эпик 2", "Описание Эпик 2");
        fileBackedTaskManager.createEpicTask(epicTask2);
        SubTask subTask1 = new SubTask("Сабтаск 1", "Описание сабтаск 1", 2);
        fileBackedTaskManager.createSubTask(subTask1);
        SubTask subTask2 = new SubTask("Сабтаск 2", "Описание сабтаск 2", 2);
        fileBackedTaskManager.createSubTask(subTask2);
        SubTask subTask3 = new SubTask("Сабтаск 3", "Описание сабтаск 3", 2);
        fileBackedTaskManager.createSubTask(subTask3);

        // Получение задач по id
        fileBackedTaskManager.getTaskById(0);
        fileBackedTaskManager.getTaskById(1);
        fileBackedTaskManager.getEpicTaskById(2);
        fileBackedTaskManager.getSubTaskById(4);
        fileBackedTaskManager.getEpicTaskById(3);
        fileBackedTaskManager.getSubTaskById(5);
        fileBackedTaskManager.getSubTaskById(4);
        fileBackedTaskManager.getEpicTaskById(3);
        fileBackedTaskManager.getSubTaskById(6);

        System.out.println("История");
        System.out.println(fileBackedTaskManager.getHistory());
        FileBackedTaskManager.historyToString(fileBackedTaskManager.getHistoryManager(), fileSave);
    }

    /**
     * Статический метод тестирования загрузки списка задач и истории просмотров из файла
     */
    public static void testLoadFromFile() {
        String fileSave = "src/storage/save.csv";
        // Создание менеджера задач по умолчанию
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(fileSave);
        // Загрузка из файла в программу
        System.out.println("Задачи");
        System.out.println(fileBackedTaskManager.getTasksRepo());
        System.out.println(fileBackedTaskManager.getEpicTasksRepo());
        System.out.println(fileBackedTaskManager.getSubTasksRepo());
        System.out.println("История");
        System.out.println(fileBackedTaskManager.getHistory());
        System.out.println("fileBackedTaskManager.getCountId() = " + fileBackedTaskManager.getCountId());
    }
}