package client;

import controller.Managers;
import exception.ManagerException;
import server.KVServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final HttpClient client;
    private final static String uri = Managers.getUri() + KVServer.PORT;
    private final String token;

    public KVTaskClient() {
        this.client = HttpClient.newHttpClient();
        this.token = register();
    }

    private String register() {
        URI url = URI.create(uri + "/register/" + "?API_TOKEN=" + token);
        HttpRequest request = HttpRequest.newBuilder().GET().uri(url).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new ManagerException("Невозможно получить данные");
        }
    }

    public void put(String key, String json) {
        URI url = URI.create(uri + "/save/" + key + "?API_TOKEN=" + token);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().POST(body).uri(url).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            response.body();
        } catch (IOException | InterruptedException e) {
            throw new ManagerException("Невозможно загрузить данные");
        }
    }

    public String load(String key) {
        URI url = URI.create(uri + "/load/" + key + "?API_TOKEN=" + token);
        HttpRequest request = HttpRequest.newBuilder().GET().uri(url).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new ManagerException("Невозможно получить данные");
        }
    }

}