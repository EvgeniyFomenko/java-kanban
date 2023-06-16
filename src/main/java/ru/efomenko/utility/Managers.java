package ru.efomenko.utility;

import ru.efomenko.service.*;

import java.io.File;
import java.net.URI;

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

    public static TaskManager getHttpTaskManager(URI uri){
        return HttpTaskManager.loadFromKvServer(uri);
    }
}
