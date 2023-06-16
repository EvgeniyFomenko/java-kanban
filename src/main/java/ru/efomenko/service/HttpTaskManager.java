package ru.efomenko.service;

import ru.efomenko.KVTaskClient;
import ru.efomenko.model.EpicTask;
import ru.efomenko.model.Subtask;
import ru.efomenko.model.Task;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpTaskManager extends FileBackedTasksManager{
    KVTaskClient kvTaskClient;
    String key;

    public HttpTaskManager(URI url) throws IOException, InterruptedException {
        super(new File("resources/test.csv"));

        kvTaskClient = new KVTaskClient(url);
        key = kvTaskClient.getToken();


    }

    public HttpTaskManager(URI url,String token) throws IOException, InterruptedException {
        super(new File("resources/test.csv"));

        kvTaskClient = new KVTaskClient(url);
        key = token;


    }
    @Override
    public void save(){
        try{
            kvTaskClient.put("save", getSerializeTask());
        }catch(IOException | InterruptedException ex){
            System.out.println(ex.getMessage());
        }
    }

    public String load() throws IOException, InterruptedException {
        return kvTaskClient.load("save");
    }
    public static HttpTaskManager loadFromKvServer(URI uri){
        String[] content;
        Map<Long, Task> taskMap = new HashMap<>();
        HttpTaskManager httpTaskManager = null;
        try {
            httpTaskManager =new HttpTaskManager(uri);
            content = httpTaskManager.load().split("\n");
        } catch (IOException | InterruptedException io) {
            return httpTaskManager;
        }

        for (int k = 1; k < content.length; k++) {
            if (content[k].isEmpty()) {
                int j = ++k;
                List<Long> historyFromString = historyFromString(content[j]);
                for (Long id : historyFromString) {
                    httpTaskManager.saveToHistory(taskMap.get(id));
                }
                return httpTaskManager;
            }

            Task task = fromString(content[k]);

            taskMap.put(task.getId(), task);
            if (task instanceof EpicTask) {
                httpTaskManager.epicStorage.saveEpicTask((EpicTask) task);
            } else if (task instanceof Subtask) {
                httpTaskManager.sortedTasks.add(task);
                httpTaskManager.subTaskStorage.putSubtask((Subtask) task);
            } else {
                httpTaskManager.sortedTasks.add(task);
                httpTaskManager.taskStorage.saveTask(task);
            }
            if (task.getId() > httpTaskManager.idTask) {
                httpTaskManager.idTask = task.getId();
            }
        }
        return httpTaskManager;
    }

}
