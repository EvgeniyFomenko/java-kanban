package ru.efomenko;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

public class KVTaskClient {
    private final URI uri;
    private String token;
    HttpRequest httpRequest;
    HttpClient httpClient;

    public KVTaskClient(URI url) throws IOException, InterruptedException {
        uri = url;
        httpClient = HttpClient.newHttpClient();
        httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uri + "/register"))
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String body = response.body();

        if (Objects.nonNull(body)) {
            token = body;
        } else {
            System.out.println("Ключ не был получен");
        }
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create(uri+"/save/"+key+"?API_TOKEN="+token)).build();
        HttpResponse<String> response = httpClient.send(httpRequest,HttpResponse.BodyHandlers.ofString());

        System.out.println(response.statusCode());
    }

    public String load(String key) throws IOException, InterruptedException {
        httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uri+"/load/"+key+"?API_TOKEN="+token)).build();

        HttpResponse<String> response = httpClient.send(httpRequest,HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public String getToken() {
        return token;
    }
}
