package ru.efomenko.service.storage;

import ru.efomenko.model.Subtask;

import java.util.HashMap;
import java.util.Map;

public class SubTaskStorage {
    Map<Long, Subtask> subtaskMap;

    public SubTaskStorage() {
        subtaskMap = new HashMap<>();
    }

    public Subtask getSubTaskById(Long taskId){
        return subtaskMap.get(taskId);
    }

    public void putSubtask(Subtask subtask){
        subtaskMap.put(subtask.getId(), subtask);
    }

    public void deleteSubtaskById(Long id){
        subtaskMap.remove(id);
    }

    public Map<Long,Subtask> getSubtaskHashMap(){
        return subtaskMap;
    }
}
