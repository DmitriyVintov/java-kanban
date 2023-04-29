package utilite;

import controller.FileBackedTaskManager;
import controller.InMemoryTaskManager;
import controller.Managers;
import controller.TaskManager;
import model.EpicTask;
import model.SubTask;
import model.Task;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Утилитарный класс для тестирования приложения
 */
public final class Test {
    static String path = Managers.getPath();
//    static TaskManager taskManager = Managers.getDefaultTaskManager();
    static TaskManager taskManager = Managers.getFileBackedTaskManager();

    private Test() {
    }

    /**
     * Статический метод тестирования CRUD методов всех типов задач
     */
    public static void testTasksCRUD() {
        createTasks();

        // Проверка поиска задач по id
//        System.out.println("\nПроверка поиска по id");
//        System.out.println("managerTasks.getTaskById(taskN1) = " + inMemoryTaskManager.getTaskById(taskN1));
//        System.out.println("managerTasks.getEpicTaskById(epicN1) = " + inMemoryTaskManager.getEpicTaskById(epicN1));
//        System.out.println("managerTasks.getSubTaskById(subN2) = " + inMemoryTaskManager.getSubTaskById(subN2));

        // Изменение статусов
//        System.out.println("\nИзменение статусов");
//        inMemoryTaskManager.updateTask(newT2, Status.DONE);
//        inMemoryTaskManager.updateSubTask(newS1, Status.IN_PROGRESS);
//        inMemoryTaskManager.updateSubTask(newS2, Status.DONE);
//        inMemoryTaskManager.updateSubTask(newS3, Status.DONE);
//        inMemoryTaskManager.updateSubTask(newS4, Status.DONE);

        // Проверка изменения статусов задач
//        System.out.println("\nПроверка изменения статусов");
//        System.out.println("managerTasks.getTasksRepo() = " + inMemoryTaskManager.getTasksRepo());
//        System.out.println("managerTasks.getEpicTasksRepo() = " + inMemoryTaskManager.getEpicTasksRepo());
//        System.out.println("managerTasks.getSubTasksRepo() = " + inMemoryTaskManager.getSubTasksRepo());

        // Удаление задач по id
//        System.out.println("\nУдаление по id");
//        inMemoryTaskManager.deleteTaskById(taskN2);
//        inMemoryTaskManager.deleteEpicTaskById(epicN2);
//        inMemoryTaskManager.deleteSubTaskById(subN2);

        // Проверка удаления задач по id
//        System.out.println("\nПроверка удаления по id");
//        System.out.println("managerTasks.getTasksRepo() = " + inMemoryTaskManager.getTasksRepo());
//        System.out.println("managerTasks.getEpicTasksRepo() = " + inMemoryTaskManager.getEpicTasksRepo());
//        System.out.println("managerTasks.getSubTasksRepo() = " + inMemoryTaskManager.getSubTasksRepo());

        // Удаление всех задач
//        System.out.println("\nУдаление всех задач");
//        inMemoryTaskManager.deleteAllTasks();
//        inMemoryTaskManager.deleteAllEpicTasks();
//        inMemoryTaskManager.deleteAllSubTasks();

        // Проверка удаления всех задач
//        System.out.println("\nПроверка удаления всех задач");
//        System.out.println("managerTasks.getTasksRepo() = " + inMemoryTaskManager.getTasksRepo());
//        System.out.println("managerTasks.getEpicTasksRepo() = " + inMemoryTaskManager.getEpicTasksRepo());
//        System.out.println("managerTasks.getSubTasksRepo() = " + inMemoryTaskManager.getSubTasksRepo());
    }

    /**
     * Статический метод тестирования создания задач
     */
    public static void createTasks() {
        //Инициализация задач
        Task newT1 = new Task("task1", "descrTask1", "26.04.2023 17:00", 5);
        Task newT2 = new Task("task2", "descrTask2", "26.04.2023 15:00", 20);
        EpicTask newE1 = new EpicTask("epic1", "descrEpic1", "", 10);
        EpicTask newE2 = new EpicTask("epic2", "descrEpic2", "", 2);

        // Создание задач
        int taskN1 = taskManager.createTask(newT1);
        int taskN2 = taskManager.createTask(newT2);
        int epicN1 = taskManager.createEpicTask(newE1);
        int subN1 = taskManager.createSubTask(new SubTask("sub1", "descrSub1", "26.04.2023 16:00", 20, epicN1));
        int subN2 = taskManager.createSubTask(new SubTask("sub2", "descrSub2", "26.04.2023 16:59", 10, epicN1));
        int epicN2 = taskManager.createEpicTask(newE2);
    }

    /**
     * Статический метод тестирования создания истории просмотров задач
     */
    public static void getTasks() {
        // Получение задач по id
        taskManager.getTaskById(0);
        taskManager.getTaskById(1);
        taskManager.getEpicTaskById(2);
        taskManager.getSubTaskById(4);
        taskManager.getEpicTaskById(5);
        taskManager.getSubTaskById(3);
        taskManager.getSubTaskById(4);
        taskManager.getEpicTaskById(2);
    }

    /**
     * Статический метод тестирования сохранения списка задач и истории просмотров в файл
     */
    public static void testSaveToFile() throws IOException {
        InMemoryTaskManager.loadMapBusyTime();
        createTasks();
        getTasks();
//        System.out.println(InMemoryTaskManager.busyTime);
//        System.out.println(taskManager.getTasksRepo());
//        System.out.println(taskManager.getEpicTasksRepo());
//        System.out.println(taskManager.getSubTasksRepo());
        System.out.println(taskManager.getPrioritizedTasks());
//        System.out.println("История");
//        System.out.println(taskManager.getHistory());
//        FileBackedTaskManager.historyToString(taskManager.getHistoryManager(), path);
    }

    /**
     * Статический метод тестирования загрузки списка задач и истории просмотров из файла
     */
    public static void testLoadFromFile() {
        // Создание менеджера задач по умолчанию
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(path);
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