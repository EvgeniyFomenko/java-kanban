package ru.efomenko.service;

import ru.efomenko.model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    private static final int HISTORY_CAPACITY = 10;
    private final List<Task> history;

    public InMemoryHistoryManager(){
        history = new ArrayList<>();
    }

    @Override
    public void addTask(Task task) {
        history.add(task);
        if (history.size() == HISTORY_CAPACITY){
            history.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {
        return  history;
    }
}
