package server;

import com.sun.net.httpserver.HttpServer;
import controller.HttpTaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private final HttpServer httpServer;
    public static final int PORT = 8080;

    public HttpTaskServer(HttpTaskManager manager) throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler(manager));
        httpServer.createContext("/tasks/task/", new TaskHandler(manager));
        httpServer.createContext("/tasks/epic/", new EpicTaskHandler(manager));
        httpServer.createContext("/tasks/subtask/", new SubTaskHandler(manager));
        httpServer.createContext("/tasks/task/?id=%d", new TaskHandler(manager));
        httpServer.createContext("/tasks/epic/?id=%d", new EpicTaskHandler(manager));
        httpServer.createContext("/tasks/subtask/?id=%d", new SubTaskHandler(manager));
        httpServer.createContext("/tasks/history/", new HistoryHandler(manager));
    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
    }
}