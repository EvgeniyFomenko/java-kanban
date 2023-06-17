package ru.efomenko.service;

import com.sun.net.httpserver.HttpServer;
import ru.efomenko.handlers.TaskHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private final HttpServer httpServer;
    private final TaskManager taskManager;

    public HttpTaskServer(int port, TaskManager taskManager) throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(port) ,0);
        this.taskManager = taskManager;
    }

    public void start(){
        httpServer.createContext("/tasks/",new TaskHandler(taskManager));
        httpServer.start();
        System.out.println("Сервер запущен");
    }

    public void stop(int delay){
        httpServer.stop(delay);
    }



}
