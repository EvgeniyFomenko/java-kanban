package ru.efomenko.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.efomenko.handlers.adapters.DurationAdapter;
import ru.efomenko.handlers.adapters.LocalDateTimeAdapter;
import ru.efomenko.http.KVServer;
import ru.efomenko.model.EpicTask;
import ru.efomenko.model.Status;
import ru.efomenko.model.Subtask;
import ru.efomenko.model.Task;
import ru.efomenko.utility.Managers;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.Calendar.JUNE;
import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {
    HttpTaskServer taskServer;
    HttpClient httpClient;
    URI uri;
    HttpRequest httpRequest;
    KVServer kvServer;
    Gson gson;

    @BeforeEach
    public void startServer() throws IOException {
        int kvPort = 8078;
        kvServer = new KVServer(kvPort);
        kvServer.start();
        int port = 8080;
        TaskManager taskManager = Managers.getHttpTaskManager(URI.create("http://localhost:8078"));
        taskServer = new HttpTaskServer(port, taskManager);
        taskServer.start();
        httpClient = HttpClient.newHttpClient();
        gson = new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

    }

    @AfterEach
    public void stopServer(){
        taskServer.stop(1);
        kvServer.stop(1);
        System.out.println("Сервер выключен");
    }

    @Test
    public void getTask() throws URISyntaxException, IOException, InterruptedException {
        LocalDateTime dateTimeOfTwos = LocalDateTime.of(2023, JUNE, 2, 22, 22);
        Task task = new Task("Task123","Discription", Status.NEW,dateTimeOfTwos,5);
        String taskJson = gson.toJson(task);
        uri = new URI("http://localhost:8080/tasks/task/");
        HttpRequest.BodyPublisher bodyPublishers = HttpRequest.BodyPublishers.ofString(taskJson);
        httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(bodyPublishers)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type","application/json")
                .build();
        HttpResponse<String> response1 = httpClient.send(httpRequest,HttpResponse.BodyHandlers.ofString());

        uri = new URI("http://localhost:8080/tasks/");
        httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .header("Accept","application/json")
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest,HttpResponse.BodyHandlers.ofString());

        assertEquals(200,response.statusCode());
        Type listType = new TypeToken<List<Task>>(){}.getType();
        List<Task> prioritize = gson.fromJson(response.body(),listType);
        assertEquals(1,prioritize.size());
    }

    @Test
    public void get404() throws URISyntaxException, InterruptedException, IOException{
        uri = new URI("http://localhost:8080/tas/");
        httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest,HttpResponse.BodyHandlers.ofString());

        assertEquals(404,response.statusCode());
    }

    @Test
    public void postTask() throws URISyntaxException, InterruptedException, IOException {
        LocalDateTime dateTimeOfTwos = LocalDateTime.of(2023, JUNE, 2, 22, 22);
        Task task = new Task("Task123","Discription", Status.NEW,dateTimeOfTwos,5);
        String taskJson = gson.toJson(task);
        uri = new URI("http://localhost:8080/tasks/task/");
        HttpRequest.BodyPublisher bodyPublishers = HttpRequest.BodyPublishers.ofString(taskJson);
        httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(bodyPublishers)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type","application/json")
                .build();
        HttpResponse<String> response1 = httpClient.send(httpRequest,HttpResponse.BodyHandlers.ofString());

        uri = new URI("http://localhost:8080/tasks/task");
        httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .header("Content-Type","application/json")
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest,HttpResponse.BodyHandlers.ofString());

        Type listType = new TypeToken<List<Task>>(){}.getType();
        List<Task> taskList = gson.fromJson(response.body(), listType);

        assertNotNull(taskList);

        uri = new URI("http://localhost:8080/tasks/task/?id=1");
        httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .header("Content-Type","application/json")
                .build();
        HttpResponse<String> responseDelete = httpClient.send(httpRequest,HttpResponse.BodyHandlers.ofString());

        assertEquals(200,responseDelete.statusCode());

        uri = new URI("http://localhost:8080/tasks/task");
        httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .header("Content-Type","application/json")
                .build();
        HttpResponse<String> responseBeforeDelete = httpClient.send(httpRequest,HttpResponse.BodyHandlers.ofString());

        List<Task> taskList1 = gson.fromJson(responseBeforeDelete.body(), listType);
        assertTrue(taskList1.isEmpty());

    }

    @Test
    public void postEpic() throws URISyntaxException, InterruptedException, IOException {
        EpicTask task = new EpicTask("Task123","Discription",Status.NEW);
        String taskJson = gson.toJson(task);
        uri = new URI("http://localhost:8080/tasks/epic/");
        HttpRequest.BodyPublisher bodyPublishers = HttpRequest.BodyPublishers.ofString(taskJson);
        httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(bodyPublishers)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type","application/json")
                .build();
        HttpResponse<String> response1 = httpClient.send(httpRequest,HttpResponse.BodyHandlers.ofString());

        assertEquals(200,response1.statusCode());

        uri = new URI("http://localhost:8080/tasks/epic");
        httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .header("Content-Type","application/json")
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest,HttpResponse.BodyHandlers.ofString());

        Type listType = new TypeToken<List<Task>>(){}.getType();
        List<Task> taskList = gson.fromJson(response.body(), listType);

        assertFalse(taskList.isEmpty());

        uri = new URI("http://localhost:8080/tasks/epic/?id=1");
        httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .header("Content-Type","application/json")
                .build();
        HttpResponse<String> responseDelete = httpClient.send(httpRequest,HttpResponse.BodyHandlers.ofString());

        assertEquals(200,responseDelete.statusCode());

        uri = new URI("http://localhost:8080/tasks/epic");
        httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .header("Content-Type","application/json")
                .build();
        HttpResponse<String> responseBeforeDelete = httpClient.send(httpRequest,HttpResponse.BodyHandlers.ofString());

        List<Task> taskList1 = gson.fromJson(responseBeforeDelete.body(), listType);
        assertTrue(taskList1.isEmpty());

    }
    @Test
    public void postSubtask() throws URISyntaxException, InterruptedException, IOException {
        EpicTask task = new EpicTask("Task123","Discription",Status.NEW);
        Subtask taskSub = new Subtask(1,"Test task","description", Status.NEW);
        Subtask taskSub1 = new Subtask(1,"Test task","description", Status.NEW);
        String taskJson = gson.toJson(task);

        HttpResponse<String> response = post(task,new URI("http://localhost:8080/tasks/epic/"));
        assertEquals(200,response.statusCode());
        response = post(taskSub,new URI("http://localhost:8080/tasks/subtask/"));
        assertEquals(200,response.statusCode());
        response = post(taskSub1,new URI("http://localhost:8080/tasks/subtask/"));
        assertEquals(200,response.statusCode());

        response = get(new URI("http://localhost:8080/tasks/epic"));
        Type listType = new TypeToken<List<Task>>(){}.getType();
        List<Task> taskList = gson.fromJson(response.body(), listType);

        assertFalse(taskList.isEmpty());

        response = get(new URI("http://localhost:8080/tasks/subtask"));
        listType = new TypeToken<List<Subtask>>(){}.getType();
        taskList = gson.fromJson(response.body(), listType);
        System.out.println(taskList);
        assertFalse(taskList.isEmpty());

        uri = new URI("http://localhost:8080/tasks/epic/?id=1");
        httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .header("Content-Type","application/json")
                .build();
        HttpResponse<String> responseDelete = httpClient.send(httpRequest,HttpResponse.BodyHandlers.ofString());

        assertEquals(200,responseDelete.statusCode());

        uri = new URI("http://localhost:8080/tasks/epic");
        httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .header("Content-Type","application/json")
                .build();
        HttpResponse<String> responseBeforeDelete = httpClient.send(httpRequest,HttpResponse.BodyHandlers.ofString());

        List<Task> taskList1 = gson.fromJson(responseBeforeDelete.body(), listType);
        assertTrue(taskList1.isEmpty());

    }

    @Test
    public void taskFromJson(){
        LocalDateTime dateTimeOfTwos = LocalDateTime.of(2023, JUNE, 2, 22, 22);
        Task task = new Task("Task123","Discription", Status.NEW,dateTimeOfTwos,5);
        Task task1 = new Task("132","Discription", Status.NEW,dateTimeOfTwos,5);
        List<Task> taskList = List.of(task,task1);
        String taskJson = gson.toJson(taskList);
        Type listType = new TypeToken<List<Task>>(){}.getType();
        List<Task> task12 = gson.fromJson(taskJson,listType);

        System.out.println(task12);

    }

    public HttpResponse<String> post(Task task,URI uri) throws IOException, InterruptedException {
        String taskJson = gson.toJson(task);
        HttpRequest.BodyPublisher bodyPublishers = HttpRequest.BodyPublishers.ofString(taskJson);
        httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(bodyPublishers)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type","application/json")
                .build();
        return httpClient.send(httpRequest,HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> get( URI uri) throws IOException, InterruptedException {
        httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .header("Content-Type","application/json")
                .build();
        return httpClient.send(httpRequest,HttpResponse.BodyHandlers.ofString());
    }
}
