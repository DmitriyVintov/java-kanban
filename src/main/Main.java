import client.KVTaskClient;
import controller.HttpTaskManager;
import controller.InMemoryTaskManager;
import server.HttpTaskServer;
import server.KVServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        InMemoryTaskManager.loadMapBusyTime();
        KVServer kvServer = new KVServer();
        kvServer.start();
        KVTaskClient kvTaskClient = new KVTaskClient();
        HttpTaskManager manager = new HttpTaskManager(kvTaskClient);
        HttpTaskServer taskServer = new HttpTaskServer(manager);
        taskServer.start();
    }
}