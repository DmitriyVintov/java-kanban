package server;

import com.sun.net.httpserver.HttpExchange;
import controller.HttpTaskManager;

import java.io.IOException;

public class HistoryHandler extends AbstractHandler {

    public HistoryHandler(HttpTaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            if (method.equals("GET") && exchange.getRequestURI().toString().contains("history")) {
                sendText(exchange, gson.toJson(manager.getHistory()), 200);
            } else {
                sendText(exchange, "Неправильный Http метод", 405);
            }
        } finally {
            exchange.close();
        }
    }
}