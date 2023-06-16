package ru.efomenko.service;

import com.sun.net.httpserver.HttpServer;
import ru.efomenko.handlers.TaskHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    HttpServer httpServer;

    public HttpTaskServer() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(8080) ,0);
    }

    public void start(){
        httpServer.createContext("/tasks/",new TaskHandler());
        httpServer.start();
        System.out.println("Сервер запущен");
    }

    public void stop(int delay){
        httpServer.stop(delay);
    }



}
