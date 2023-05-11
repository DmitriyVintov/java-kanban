package server;

import client.KVTaskClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import controller.HistoryManager;
import controller.HttpTaskManager;
import controller.Managers;
import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpTaskServerTest {
    private HttpTaskManager taskManager;
    private final Gson gson = new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new AbstractHandler.LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new AbstractHandler.DurationAdapter())
            .registerTypeAdapter(List.class, new AbstractHandler.CollectionAdapter())
            .create();
    private Task task;
    private EpicTask epicTask;
    private SubTask subTask;
    private Task taskInProgress;
    private Task taskDone;
    private SubTask subTaskInProgress;
    private SubTask subTaskDone;
    private HistoryManager<Task> historyManager;
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
        KVTaskClient kvTaskClient = new KVTaskClient();
        taskManager = new HttpTaskManager(kvTaskClient);
        controller.Test.loadMapBusyTime();
        task = new Task("task1", "descrTask1", "29.04.2023 12:00", 10);
        taskInProgress = new Task("task1", "descrTask1", "29.04.2023 12:00", 10, Status.IN_PROGRESS);
        taskDone = new Task("task1", "descrTask1", "29.04.2023 12:00", 10, Status.DONE);
        epicTask = new EpicTask("epic1", "descrEpic1", "29.04.2023 18:00", 5);
        subTask = new SubTask("subtask1", "descrSub1", "29.04.2023 15:00", 30, epicTask.getId());
        subTaskInProgress = new SubTask("subtask1", "descrSub1", "29.04.2023 15:00", 30, epicTask.getId(), Status.IN_PROGRESS);
        subTaskDone = new SubTask("subtask1", "descrSub1", "29.04.2023 15:00", 30, epicTask.getId(), Status.DONE);
        historyManager = taskManager.getHistoryManager();
        httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
    }

    @AfterEach
    void stop() {
        kvServer.stop();
        httpTaskServer.stop();
    }

    private HttpResponse<String> getResponse(String path) throws IOException, InterruptedException {
        URI url = URI.create(uri + HTTP_SERVER_PORT + path);
        HttpRequest request = HttpRequest.newBuilder().GET().uri(url).build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private void postTask(String path, Task task) throws IOException, InterruptedException {
        URI url = URI.create(uri + HTTP_SERVER_PORT + path);
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task))).uri(url).build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> deleteTask(String path) throws IOException, InterruptedException {
        URI url = URI.create(uri + HTTP_SERVER_PORT + path);
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(url).build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private List<Task> tasks(String json) {
        return gson.fromJson(json, new TypeToken<ArrayList<Task>>() {
        }.getType());
    }

    private List<EpicTask> epics(String json) {
        return gson.fromJson(json, new TypeToken<ArrayList<EpicTask>>() {
        }.getType());
    }

    private List<SubTask> subTasks(String json) {
        return gson.fromJson(json, new TypeToken<ArrayList<SubTask>>() {
        }.getType());
    }

    @Test
    void shouldGetPrioritizedTasks() throws IOException, InterruptedException {
        postTask("/tasks/task/", task);
        HttpResponse<String> response = getResponse("/tasks");
        assertEquals(200, response.statusCode());
        assertEquals(1, tasks(response.body()).size());
    }

    @Test
    void shouldGetTasksList() throws IOException, InterruptedException {
        postTask("/tasks/task/", task);
        HttpResponse<String> response = getResponse("/tasks/task");
        assertEquals(200, response.statusCode());
        assertEquals(1, tasks(response.body()).size());
        assertEquals(Task.class, tasks(response.body()).get(0).getClass());
    }

    @Test
    void shouldGetEpicTasksList() throws IOException, InterruptedException {
        postTask("/tasks/epic/", epicTask);
        HttpResponse<String> response = getResponse("/tasks/epic/");
        assertEquals(200, response.statusCode());
        assertEquals(1, epics(response.body()).size());
        assertEquals(EpicTask.class, epics(response.body()).get(0).getClass());
    }

    @Test
    void shouldGetSubTasksList() throws IOException, InterruptedException {
        postTask("/tasks/epic/", epicTask);
        postTask("/tasks/subtask/", subTask);
        HttpResponse<String> response = getResponse("/tasks/subtask/");
        assertEquals(200, response.statusCode());
        assertEquals(1, subTasks(response.body()).size());
        assertEquals(SubTask.class, subTasks(response.body()).get(0).getClass());
    }

    @Test
    void shouldGetTaskById() throws IOException, InterruptedException {
        postTask("/tasks/task/", task);
        HttpResponse<String> response = getResponse("/tasks/task/?id=0");
        assertEquals(200, response.statusCode());
        JsonElement element = JsonParser.parseString(new String(response.body().getBytes(), StandardCharsets.UTF_8));
        int id = Integer.parseInt(String.valueOf(element.getAsJsonObject().get("id")));
        assertEquals(0, id);
    }

    @Test
    void shouldGetEpicTaskById() throws IOException, InterruptedException {
        postTask("/tasks/epic/", epicTask);
        HttpResponse<String> response = getResponse("/tasks/epic/?id=0");
        assertEquals(200, response.statusCode());
        JsonElement element = JsonParser.parseString(new String(response.body().getBytes(), StandardCharsets.UTF_8));
        int id = Integer.parseInt(String.valueOf(element.getAsJsonObject().get("id")));
        assertEquals(0, id);
    }

    @Test
    void shouldGetSubTaskById() throws IOException, InterruptedException {
        postTask("/tasks/epic/", epicTask);
        postTask("/tasks/subtask/", subTask);
        HttpResponse<String> response = getResponse("/tasks/subtask/?id=1");
        assertEquals(200, response.statusCode());
        JsonElement element = JsonParser.parseString(new String(response.body().getBytes(), StandardCharsets.UTF_8));
        int id = Integer.parseInt(String.valueOf(element.getAsJsonObject().get("id")));
        assertEquals(1, id);
    }

    @Test
    void shouldBeUpdatedTask() throws IOException, InterruptedException {
        postTask("/tasks/task/", task);
        postTask("/tasks/task/", taskInProgress);
        HttpResponse<String> response = getResponse("/tasks/task/?id=0");
        assertEquals(200, response.statusCode());
        JsonElement element = JsonParser.parseString(new String(response.body().getBytes(), StandardCharsets.UTF_8));
        assertEquals("IN_PROGRESS", element.getAsJsonObject().get("status").toString().replace("\"", ""));
    }

    @Test
    void shouldBeUpdatedEpicTaskAndSubTask() throws IOException, InterruptedException {
        postTask("/tasks/epic/", epicTask);
        postTask("/tasks/subtask/", subTask);
        postTask("/tasks/subtask/", subTaskInProgress);
        HttpResponse<String> response = getResponse("/tasks/epic/?id=0");
        HttpResponse<String> response1 = getResponse("/tasks/subtask/?id=1");
        assertEquals(200, response.statusCode());
        assertEquals(200, response1.statusCode());
        JsonElement element = JsonParser.parseString(new String(response.body().getBytes(), StandardCharsets.UTF_8));
        JsonElement element1 = JsonParser.parseString(new String(response1.body().getBytes(), StandardCharsets.UTF_8));
        assertEquals("IN_PROGRESS", element.getAsJsonObject().get("status").toString().replace("\"", ""));
        assertEquals("IN_PROGRESS", element1.getAsJsonObject().get("status").toString().replace("\"", ""));
    }

    @Test
    void shouldBeDeletedTaskById() throws IOException, InterruptedException {
        postTask("/tasks/task/", task);
        HttpResponse<String> response = deleteTask("/tasks/task/?id=0");
        assertEquals(200, response.statusCode());
        assertEquals("Задача удалена", response.body());
    }

    @Test
    void shouldBeDeletedEpicTaskById() throws IOException, InterruptedException {
        postTask("/tasks/epic/", epicTask);
        HttpResponse<String> response = deleteTask("/tasks/epic/?id=0");
        assertEquals(200, response.statusCode());
        assertEquals("Задача удалена", response.body());
    }

    @Test
    void shouldBeDeletedSubTaskById() throws IOException, InterruptedException {
        postTask("/tasks/epic/", epicTask);
        postTask("/tasks/subtask/", subTask);
        HttpResponse<String> response = deleteTask("/tasks/subtask/?id=1");
        assertEquals(200, response.statusCode());
        assertEquals("Задача удалена", response.body());
    }

    @Test
    void shouldBeDeletedAllTasks() throws IOException, InterruptedException {
        postTask("/tasks/task/", task);
        HttpResponse<String> response = deleteTask("/tasks/task/");
        assertEquals(200, response.statusCode());
        assertEquals("Все задачи удалены", response.body());
    }

    @Test
    void shouldBeDeletedAllEpicTasks() throws IOException, InterruptedException {
        postTask("/tasks/epic/", epicTask);
        HttpResponse<String> response = deleteTask("/tasks/epic/");
        assertEquals(200, response.statusCode());
        assertEquals("Все задачи удалены", response.body());
        assertEquals(0, taskManager.getEpicTasksRepo().size());
    }

    @Test
    void shouldBeDeletedAllSubTasks() throws IOException, InterruptedException {
        postTask("/tasks/epic/", epicTask);
        postTask("/tasks/subtask/", subTask);
        HttpResponse<String> response = deleteTask("/tasks/subtask/");
        assertEquals(200, response.statusCode());
        assertEquals("Все задачи удалены", response.body());
    }

    @Test
    void shouldGetHistory() throws IOException, InterruptedException {
        postTask("/tasks/task/", task);
        getResponse("/tasks/task/?id=0");
        HttpResponse<String> response = getResponse("/tasks/history/");
        ArrayList<Task> history = gson.fromJson(response.body(), new TypeToken<ArrayList<Task>>() {
        }.getType());
        assertEquals(200, response.statusCode());
        assertEquals(1, history.size());
    }

}