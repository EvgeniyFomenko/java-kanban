package ru.efomenko.service.storage;

import ru.efomenko.model.Task;

import java.util.HashMap;

public class TaskStorage {
    private final HashMap<Long, Task> taskHashMap;

    public TaskStorage() {
        taskHashMap = new HashMap<>();
    }

    public HashMap<Long, Task> getTaskHashMap() {
        return taskHashMap;
    }

    public Task getTaskById(long id){
      return taskHashMap.get(id);
    }
    public void saveTask(Task task){
       taskHashMap.put(task.getId(),task);
    }
    public void deleteTaskById(long id){
        taskHashMap.remove(id);
    }
}
