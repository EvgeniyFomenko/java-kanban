package ru.efomenko.utility;

import ru.efomenko.service.HistoryManager;
import ru.efomenko.service.InMemoryHistoryManager;
import ru.efomenko.service.InMemoryTaskManager;
import ru.efomenko.service.TaskManager;

public class Managers {
    public static TaskManager getDefault(){
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistoryManager(){
        return new InMemoryHistoryManager();
    }
}
