package server;

import com.sun.net.httpserver.HttpExchange;
import controller.HttpTaskManager;
import model.EpicTask;
import model.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class EpicTaskHandler extends AbstractHandler {

    public EpicTaskHandler(HttpTaskManager manager) {
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
                            && split[2].equals("epic")) {
                        sendText(exchange, gson.toJson(new ArrayList<>(manager.getEpicTasksRepo().values())), 200);
                        return;
                    }
                    if (exchange.getRequestURI().toString().contains("?id=")) {
                        int queryId = Integer.parseInt(exchange.getRequestURI().getRawQuery().split("=")[1]);
                        try {
                            sendText(exchange, gson.toJson(manager.getEpicTaskById(queryId)), 200);
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
                        EpicTask epicTask = gson.fromJson(s, EpicTask.class);
                        manager.createEpicTask(epicTask);
                        sendText(exchange, gson.toJson(new ArrayList<Task>(manager.getEpicTasksRepo().values())), 200);
                    } catch (Exception e) {
                        System.out.println(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
                        sendText(exchange, "Невозможно создать задачу", 400);
                    }
                    break;
                case "DELETE":
                    if ((split.length == 3) && !exchange.getRequestURI().toString().contains("?")) {
                        manager.deleteAllEpicTasks();
                        sendText(exchange, "Все задачи удалены", 200);
                        return;
                    }
                    if (exchange.getRequestURI().toString().contains("?id=")) {
                        int queryId = Integer.parseInt(exchange.getRequestURI().getRawQuery().split("=")[1]);
                        try {
                            manager.deleteEpicTaskById(queryId);
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