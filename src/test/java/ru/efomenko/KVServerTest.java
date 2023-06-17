package ru.efomenko;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.efomenko.handlers.adapters.DurationAdapter;
import ru.efomenko.handlers.adapters.LocalDateTimeAdapter;
import ru.efomenko.http.KVServer;
import ru.efomenko.http.KVTaskClient;
import ru.efomenko.model.Status;
import ru.efomenko.model.Task;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;

import static java.util.Calendar.JUNE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class KVServerTest {
    KVServer kvServer;
    KVTaskClient kvTaskClient;
    Gson gson;

    @BeforeEach
    public void start() throws IOException, InterruptedException {
        int kvPort = 8078;
        kvServer =new KVServer(kvPort);
        kvServer.start();

        kvTaskClient = new KVTaskClient(URI.create("http://localhost:8078"));
    }

    @AfterEach
    public void stop(){
        kvServer.stop(1);
    }

    @Test
    public void getToken(){
        String token =  kvTaskClient.getToken();

        assertNotNull(token);
    }

    @Test
    public void postTask() throws IOException, InterruptedException {
        LocalDateTime dateTimeOfTwos = LocalDateTime.of(2023, JUNE, 2, 22, 22);
        Task task = new Task("Task123","Discription", Status.NEW,dateTimeOfTwos,5);
        gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        String taskJson = gson.toJson(task);
        String key = "1";

        kvTaskClient.put(key, taskJson);
        String loadTask = kvTaskClient.load(key);
        assertEquals(taskJson,loadTask);
    }

}