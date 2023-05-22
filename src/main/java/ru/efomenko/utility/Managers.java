package ru.efomenko.utility;

import ru.efomenko.service.*;

import java.io.File;

public class Managers {
    public static TaskManager getDefault(){
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistoryManager(){
        return new InMemoryHistoryManager();
    }

    public static TaskManager getFileBackedTaskManager(File file){
        return FileBackedTasksManager.loadFromFile(file);
    }
}
