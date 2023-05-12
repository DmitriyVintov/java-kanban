package controller;

import client.KVTaskClient;
import model.Task;
import server.KVServer;

import java.io.IOException;

/**
 * Утилитарный класс для создания менеджеров задач и истории просмотров
 */
public class Managers {
    private static final String path = "src/main/storage/save.csv";
    private static final String URI = "http://localhost:";

    private Managers() {
    }

    public static String getPath() {
        return path;
    }

    public static String getUri() {
        return URI;
    }

    /**
     * Статический метод создания менеджера задач по умолчанию
     *
     * @return InMemoryTaskManager
     */
    public static TaskManager getDefaultTaskManager() {
        return new HttpTaskManager(new KVTaskClient());
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

    public static KVServer getDefaultKVServer() throws IOException {
        KVServer kvServer = new KVServer();
        kvServer.start();
        return kvServer;
    }
}