package controller;

import client.KVTaskClient;
import com.google.gson.*;
import model.EpicTask;
import model.SubTask;
import model.Task;
import server.AbstractHandler;
import server.KVServer;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpTaskManager extends FileBackedTaskManager {
    private final KVTaskClient client;
    private final Gson gson = new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new AbstractHandler.LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new AbstractHandler.DurationAdapter())
            .registerTypeAdapter(List.class, new AbstractHandler.CollectionAdapter())
            .create();

    public HttpTaskManager(KVTaskClient client) {
        super(Managers.getUri() + KVServer.PORT);
        this.client = client;
    }

    public void load() {
        Map<String, String> loadedTasks = new HashMap<>() {{
            put("tasks", client.load("tasks"));
            put("epics", client.load("epics"));
            put("subtasks", client.load("subtasks"));
            put("history", client.load("history"));
        }};

        System.out.println(loadedTasks);
        for (Map.Entry<String, String> map : loadedTasks.entrySet()) {
            JsonElement element = JsonParser.parseString(map.getValue());
            String key = map.getKey();
            if (element.isJsonArray()) {
                JsonArray jsonArray = element.getAsJsonArray();
                switch (key) {
                    case "tasks":
                        for (JsonElement elem : jsonArray) {
                            Task task = gson.fromJson(elem, Task.class);
                            getTasksRepo().put(task.getId(), task);
                        }
                        break;
                    case "epics":
                        for (JsonElement elem : jsonArray) {
                            EpicTask epicTask = gson.fromJson(elem, EpicTask.class);
                            getEpicTasksRepo().put(epicTask.getId(), epicTask);
                        }
                        break;
                    case "subtasks":
                        for (JsonElement elem : jsonArray) {
                            SubTask subTask = gson.fromJson(elem, SubTask.class);
                            getSubTasksRepo().put(subTask.getId(), subTask);
                        }
                        break;
                    case "history":
                        for (JsonElement elem : jsonArray) {
                            int id = elem.getAsJsonObject().get("id").getAsInt();
                            if (this.subTasksRepo.containsKey(id)) {
                                this.getSubTaskById(id);
                            } else if (this.epicTasksRepo.containsKey(id)) {
                                this.getEpicTaskById(id);
                            } else if (this.tasksRepo.containsKey(id)) {
                                this.getTaskById(id);
                            }
                        }
                        break;
                }
            }

        }
    }

    @Override
    protected void save() {
        client.put("tasks", gson.toJson(new ArrayList<>(this.getTasksRepo().values())));
        client.put("epics", gson.toJson(new ArrayList<>(this.getEpicTasksRepo().values())));
        client.put("subtasks", gson.toJson(new ArrayList<>(this.getSubTasksRepo().values())));
        client.put("history", gson.toJson(this.getHistory()));
    }
}
