package ru.efomenko.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.efomenko.http.KVServer;
import ru.efomenko.model.Status;
import ru.efomenko.model.Task;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;

import static java.util.Calendar.JUNE;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HttpTaskManagerTest extends TaskManagerTest {
    KVServer httpTaskServer;
    File file = new File("resources/test.csv");
    URI uri;
    @BeforeEach
    public void start() throws IOException, InterruptedException {
        int kvPort = 8078;
        httpTaskServer = new KVServer(kvPort);
        httpTaskServer.start();
        uri = URI.create("http://localhost:8078");
        this.taskManager = new HttpTaskManager(uri);

    }

    @AfterEach
    public void stop(){
        httpTaskServer.stop(1);
    }

    @Test
    public void putTask() throws IOException, InterruptedException {
        LocalDateTime dateTimeOfTwos = LocalDateTime.of(2023, JUNE, 2, 22, 22);
        Task task = new Task("Task123","Discription", Status.NEW,dateTimeOfTwos,5);
        taskManager.createTask(task);

        taskManager = HttpTaskManager.loadFromKvServer(uri);
        Task getTask = taskManager.getTaskById(1);
        assertNotNull(getTask);
    }
}