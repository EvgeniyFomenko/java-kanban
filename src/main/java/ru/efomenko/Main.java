package ru.efomenko;

import ru.efomenko.service.HttpTaskServer;
import ru.efomenko.utility.Managers;

import java.io.IOException;
import java.net.URI;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpTaskServer taskServer = new HttpTaskServer(8080, Managers.getHttpTaskManager(URI.create("http://localhost:8078")));
        taskServer.start();
    }


    }

