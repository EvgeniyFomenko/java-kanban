package ru.efomenko.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.efomenko.exceptions.TasksTimeValidationException;
import ru.efomenko.handlers.adapters.DurationAdapter;
import ru.efomenko.handlers.adapters.LocalDateTimeAdapter;
import ru.efomenko.model.Endpoint;
import ru.efomenko.model.EpicTask;
import ru.efomenko.model.Subtask;
import ru.efomenko.model.Task;
import ru.efomenko.service.TaskManager;
import ru.efomenko.utility.Managers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class TaskHandler implements HttpHandler {
    String DEFAULT_CHARSET;
    TaskManager fileBackedTasksManager;
    Gson gson;

    public TaskHandler() {
        fileBackedTasksManager = Managers.getHttpTaskManager(URI.create("http://localhost:8078"));
        DEFAULT_CHARSET = "UTF-8";
        gson = new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        Endpoint endpoint = getEndpoint(path, method);

        switch (endpoint) {
            case GET_HISTORY:
                handleHistory(exchange);
                break;
            case GET_TASK:
                handleTask(exchange);
                break;
            case GET_EPIC:
                handleEpic(exchange);
                break;
            case GET_SUBTASK:
                handleSubtask(exchange);
                break;
            case POST_TASK:
                taskPostHandle(exchange);
                break;
            case POST_EPIC:
                epictaskPostHandle(exchange);
                break;
            case POST_SUBTASK:
                subtaskPostHandle(exchange);
                break;
            case DELETE_TASK:
                taskDeleteHandler(exchange);
                break;
            case DELETE_EPIC:
                epictaskDeleteHandler(exchange);
                break;
            case DELETE_SUBTASK:
                subtaskDeleteHandler(exchange);
                break;
            case UNKNOWN:
                writeResponse(exchange, "Неизвстный запрос", 404);
                break;
            case GET_TASKS:
                handlePrioritizeTasks(exchange);
                break;

        }
    }

    public void handlePrioritizeTasks(HttpExchange exchange) throws IOException {
        String prioritizeTasks = gson.toJson(fileBackedTasksManager.getPrioritizedTasks());
        writeResponse(exchange, prioritizeTasks, 200);
    }

    public void handleHistory(HttpExchange exchange) throws IOException {
        String history = gson.toJson(fileBackedTasksManager.getHistory());
        writeResponse(exchange, history, 200);
    }

    public void handleTask(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        String task;

        if (Objects.nonNull(query)) {
            int id = getTaskId(query);
            task = gson.toJson(fileBackedTasksManager.getTaskById(id));
            int responseCode = Objects.isNull(task) ? 404 : 200;

            writeResponse(exchange, task, responseCode);
        } else {
            task = gson.toJson(fileBackedTasksManager.getTaskList());
            int responseCode = Objects.isNull(task) ? 404 : 200;

            writeResponse(exchange, task, responseCode);
        }
    }

    public void taskPostHandle(HttpExchange exchange) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        String taskJson = bf.readLine();
        Task task;

        try {
            task = gson.fromJson(taskJson, Task.class);
        } catch (JsonSyntaxException e) {
            String response = "Получен некорректный JSON";
            writeResponse(exchange, response, 400);
            return;
        }

        try {
            if (Objects.isNull((fileBackedTasksManager.getTaskById(task.getId())))) {
                fileBackedTasksManager.createTask(task);
            } else {
                fileBackedTasksManager.updateTask(task);
            }
        } catch (TasksTimeValidationException e) {
            String response = e.getMessage();
            writeResponse(exchange, response, 400);
            return;
        }
        writeResponse(exchange, "", 200);
    }

    public void epictaskPostHandle(HttpExchange exchange) throws IOException {
        String taskJson = new String(exchange.getRequestBody().readAllBytes());
        EpicTask task;
        try {
            task = gson.fromJson(taskJson, EpicTask.class);
        } catch (JsonSyntaxException ex) {
            System.out.println(ex.getMessage());
            String response = "Получен некорректный JSON";
            writeResponse(exchange, response, 400);
            return;
        }

        try {
            if (Objects.isNull((fileBackedTasksManager.getEpicTaskById(task.getId())))) {
                fileBackedTasksManager.createEpicTask(task);
            } else {
                fileBackedTasksManager.updateEpicTask(task);
            }
        } catch (TasksTimeValidationException e) {
            String response = e.getMessage();
            writeResponse(exchange, response, 400);
            return;
        }
        writeResponse(exchange, "", 200);
    }

    public void subtaskPostHandle(HttpExchange exchange) throws IOException {
        String taskJson = new String(exchange.getRequestBody().readAllBytes());
        Subtask task;
        try {
            task = gson.fromJson(taskJson, Subtask.class);
        } catch (JsonSyntaxException ex) {
            String response = "Получен некорректный JSON";
            writeResponse(exchange, response, 400);
            return;
        }

        try {
            if (Objects.isNull((fileBackedTasksManager.getSubTaskById(task.getId())))) {
                fileBackedTasksManager.addSubtaskInEpicTask(task);
            } else {
                fileBackedTasksManager.updateSubtask(task);
            }
        } catch (TasksTimeValidationException e) {
            String response = e.getMessage();
            writeResponse(exchange, response, 400);
            return;
        }
        writeResponse(exchange, "", 200);
    }

    public void taskDeleteHandler(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getQuery();

        if (Objects.nonNull(path)) {
            int id = getTaskId(path);
            fileBackedTasksManager.deleteTaskById(id);
            int responseCode = 200;
            writeResponse(exchange, " ", responseCode);
        } else {
            fileBackedTasksManager.deleteAllTasks();
            int responseCode = 200;
            writeResponse(exchange, " ", responseCode);
        }
    }

    public void epictaskDeleteHandler(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getQuery();

        if (Objects.nonNull(path)) {
            long id = getTaskId(path);
            fileBackedTasksManager.deleteEpicTaskById(id);
            int responseCode = 200;
            writeResponse(exchange, " ", responseCode);
        } else {
            fileBackedTasksManager.deleteAllEpicTasks();
            int responseCode = 200;
            writeResponse(exchange, " ", responseCode);
        }
    }

    public void subtaskDeleteHandler(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getQuery();

        if (Objects.nonNull(path)) {
            long id = getTaskId(path);
            fileBackedTasksManager.deleteSubtaskById(id);
            int responseCode = 200;
            writeResponse(exchange, " ", responseCode);
        } else {
            fileBackedTasksManager.deleteAllSubtask();
            int responseCode = 200;
            writeResponse(exchange, " ", responseCode);
        }
    }

    public void handleEpic(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getQuery();
        String task;
        if (Objects.nonNull(path)) {
            int id = getTaskId(path);
            task = gson.toJson(fileBackedTasksManager.getEpicTaskById(id));
            int responseCode = Objects.isNull(task) ? 404 : 200;
            writeResponse(exchange, task, responseCode);
        } else {
            task = gson.toJson(fileBackedTasksManager.getEpicTaskList());
            int responseCode = Objects.isNull(task) ? 404 : 200;
            writeResponse(exchange, task, responseCode);
        }
    }

    public void handleSubtask(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getQuery();
        String task;
        if (Objects.nonNull(path)) {
            int id = getTaskId(path);
            task = gson.toJson(fileBackedTasksManager.getSubTaskById(id));
            int responseCode = Objects.isNull(task) ? 404 : 200;
            writeResponse(exchange, task, responseCode);
        } else {
            task = gson.toJson(fileBackedTasksManager.getSubtaskList());
            int responseCode = Objects.isNull(task) ? 404 : 200;
            writeResponse(exchange, task, responseCode);
        }
    }

    private void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
        if (responseString.isBlank()) {
            exchange.sendResponseHeaders(responseCode, 0);
        } else {
            byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
            exchange.sendResponseHeaders(responseCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        exchange.close();
    }

    private Endpoint getEndpoint(String path, String method) {
        String[] pathSplit = path.split("/");
        if (pathSplit.length > 2) {
            return Endpoint.valueOf(method.toUpperCase() + "_" + pathSplit[2].toUpperCase());
        } else if (pathSplit.length == 2) {
            return Endpoint.valueOf(method.toUpperCase() + "_" + pathSplit[1].toUpperCase());
        } else {
            return Endpoint.UNKNOWN;
        }
    }

    private int getTaskId(String path) {
        String id = path.split("=")[1];
        return Integer.parseInt(id);
    }


}
