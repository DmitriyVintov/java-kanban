package controller;

import client.KVTaskClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.EpicTask;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.AbstractHandler;
import server.HttpTaskServer;
import server.KVServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpTaskManagerTest {
    private final Gson gson = new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new AbstractHandler.LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new AbstractHandler.DurationAdapter())
            .registerTypeAdapter(List.class, new AbstractHandler.CollectionAdapter())
            .create();
    private HttpTaskManager taskManager;
    private Task task;
    private EpicTask epicTask;
    private static KVServer kvServer;
    private static HttpTaskServer httpTaskServer;
    private static HttpClient httpClient;
    private final static String uri = Managers.getUri();
    private final static int HTTP_SERVER_PORT = HttpTaskServer.PORT;

    @BeforeEach
    void createTasks() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        httpClient = HttpClient.newBuilder().build();
        controller.Test.loadMapBusyTime();
        KVTaskClient kvTaskClient = new KVTaskClient();
        taskManager = new HttpTaskManager(kvTaskClient);
        task = new Task("task1", "descrTask1", "29.04.2023 12:00", 10);
        epicTask = new EpicTask("epic1", "descrEpic1", "29.04.2023 18:00", 5);
        httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
    }

    @AfterEach
    void stop() {
        kvServer.stop();
        httpTaskServer.stop();
    }

    private void postTask(String path, Task task) throws IOException, InterruptedException {
        URI url = URI.create(uri + HTTP_SERVER_PORT + path);
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task))).uri(url).build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void shouldBeLoadedTasks() throws IOException, InterruptedException {
        postTask("/tasks/task/", task);
        postTask("/tasks/epic/", epicTask);
        postTask("/tasks/subtask/", new SubTask("subtask1", "descrSub1", "29.04.2023 15:00", 30, 1));
        taskManager.getTasksRepo().clear();
        taskManager.getEpicTasksRepo().clear();
        taskManager.getSubTasksRepo().clear();
        taskManager.load();
        assertEquals(1, taskManager.getTasksRepo().size());
        assertEquals(1, taskManager.getEpicTasksRepo().size());
        assertEquals(1, taskManager.getSubTasksRepo().size());
    }


}