package server;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import controller.HttpTaskManager;
import model.SubTask;

import java.io.IOException;
import java.util.ArrayList;

public class SubTaskHandler extends AbstractHandler {

    public SubTaskHandler(HttpTaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String[] split = exchange.getRequestURI().getPath().split("/");
            switch (method) {
                case "GET":
                    if ((split.length == 3) && !exchange.getRequestURI().toString().contains("?")
                            && split[2].equals("subtask")) {
                        sendText(exchange, gson.toJson(new ArrayList<>(manager.getSubTasksRepo().values())), 200);
                        return;
                    }
                    if (exchange.getRequestURI().toString().contains("?id=")) {
                        int queryId = Integer.parseInt(exchange.getRequestURI().getRawQuery().split("=")[1]);
                        try {
                            sendText(exchange, gson.toJson(manager.getSubTaskById(queryId)), 200);
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
                        SubTask subTask = null;
                        try {
                            subTask = gson.fromJson(s, SubTask.class);
                        } catch (JsonSyntaxException e) {
                            System.out.println(e.getMessage());
                        }
                        manager.createSubTask(subTask);
                        sendText(exchange, "Задача создана", 200);
                    } catch (Exception e) {
                        sendText(exchange, "Невозможно создать задачу", 400);
                    }
                    break;
                case "DELETE":
                    if ((split.length == 3) && !exchange.getRequestURI().toString().contains("?")) {
                        manager.deleteAllSubTasks();
                        sendText(exchange, "Все задачи удалены", 200);
                        return;
                    }
                    if (exchange.getRequestURI().toString().contains("?id=")) {
                        int queryId = Integer.parseInt(exchange.getRequestURI().getRawQuery().split("=")[1]);
                        try {
                            manager.deleteSubTaskById(queryId);
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