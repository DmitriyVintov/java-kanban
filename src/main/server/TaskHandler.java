package server;

import com.sun.net.httpserver.HttpExchange;
import controller.HttpTaskManager;
import model.Task;

import java.io.IOException;
import java.util.ArrayList;

public class TaskHandler extends AbstractHandler {

    public TaskHandler(HttpTaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String[] split = exchange.getRequestURI().getPath().split("/");
            switch (method) {
                case "GET":
                    if (split.length == 2) {
                        sendText(exchange, gson.toJson(manager.getPrioritizedTasks()), 200);
                        return;
                    }
                    if ((split.length == 3) && !exchange.getRequestURI().toString().contains("?")
                            && split[2].equals("task")) {
                        sendText(exchange, gson.toJson(new ArrayList<>(manager.getTasksRepo().values())), 200);
                        return;
                    }
                    if (exchange.getRequestURI().toString().contains("?id=")) {
                        int queryId = Integer.parseInt(exchange.getRequestURI().getRawQuery().split("=")[1]);
                        try {
                            sendText(exchange, gson.toJson(manager.getTaskById(queryId)), 200);
                        } catch (IOException e) {
                            sendText(exchange, "Невозможно найти задачу. Проверьте id задачи", 404);
                        }
                    } else {
                        sendText(exchange, "Неверный адрес", 403);
                    }
                    break;
                case "POST":
                    String s = readText(exchange);
                    try {
                        Task task = gson.fromJson(s, Task.class);
                        manager.createTask(task);
                        sendText(exchange, "Задача создана", 200);
                    } catch (Exception e) {
                        sendText(exchange, "Невозможно создать задачу", 400);
                    }
                    break;
                case "DELETE":
                    if ((split.length == 3) && !exchange.getRequestURI().toString().contains("?")) {
                        manager.deleteAllTasks();
                        sendText(exchange, "Все задачи удалены", 200);
                        return;
                    }
                    if (exchange.getRequestURI().toString().contains("?id=")) {
                        int queryId = Integer.parseInt(exchange.getRequestURI().getRawQuery().split("=")[1]);
                        try {
                            manager.deleteTaskById(queryId);
                            sendText(exchange, "Задача удалена", 200);
                        } catch (IOException e) {
                            sendText(exchange, "Невозможно найти задачу. Проверьте id задачи", 404);
                        }
                    } else {
                        sendText(exchange, "Неверный адрес", 403);
                    }
                    break;
                default:
                    sendText(exchange, "Неправильный Http метод", 405);
            }
        } finally {
            exchange.close();
        }
    }
}