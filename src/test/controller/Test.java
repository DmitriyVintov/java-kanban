package controller;

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

    public static void loadMapBusyTime() {
        for (long i = 0; i < InMemoryTaskManager.NUM_OF_15_MIN_INTERVALS; i++) {
            LocalDateTime start = LocalDateTime.of(2023, 1, 1, 0, 0);
            InMemoryTaskManager.busyTime.put(start.plusMinutes(i * 15), false);
        }
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
        taskManager.createTask(newT1);
        taskManager.createTask(newT2);
        int epicN1 = taskManager.createEpicTask(newE1);
        taskManager.createSubTask(new SubTask("sub1", "descrSub1", "26.04.2023 16:00", 20, epicN1));
        taskManager.createSubTask(new SubTask("sub2", "descrSub2", "26.04.2023 16:59", 10, epicN1));
        taskManager.createEpicTask(newE2);
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
        Test.loadMapBusyTime();
        createTasks();
        getTasks();
        System.out.println(taskManager.getTasksRepo());
        System.out.println(taskManager.getEpicTasksRepo());
        System.out.println(taskManager.getSubTasksRepo());
        System.out.println(taskManager.getPrioritizedTasks());
        System.out.println("История");
        System.out.println(taskManager.getHistory());
        FileBackedTaskManager.historyToString(taskManager.getHistoryManager(), path);
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

    public static void main(String[] args) throws IOException {
        testSaveToFile();
//        testLoadFromFile();
    }
}