package ru.efomenko.storage;

import ru.efomenko.model.EpicTask;

import java.util.HashMap;

public class EpicStorage {
    private final HashMap<Long, EpicTask> epicTaskHashMap;

    public EpicStorage() {
        epicTaskHashMap = new HashMap<>();
    }

    public void saveEpicTask( EpicTask epicTask) {
        epicTaskHashMap.put(epicTask.getId(), epicTask);

    }
    public EpicTask getEpicTaskById(Long id){
        return epicTaskHashMap.get(id);
    }

    public HashMap<Long, EpicTask> getEpicTaskHashMap() {
        return epicTaskHashMap;
    }

    public void deleteEpicTaskById(Long id){
        epicTaskHashMap.remove(id);
    }
}
