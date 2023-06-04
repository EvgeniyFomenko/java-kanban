package ru.efomenko.service;

import ru.efomenko.exceptions.ManagerLoaderException;
import ru.efomenko.exceptions.ManagerSaveException;
import ru.efomenko.model.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    private final File file;

    public FileBackedTasksManager(File fileName) {
        this.file = fileName;
    }

    public static List<Long> historyFromString(String value) {
        String[] histry = value.split(",");
        List<Long> historyList = new ArrayList<>();

        for (String h : histry) {
            historyList.add(Long.valueOf(h));
        }
        return historyList;
    }

    protected void saveToHistory(Task task) {
        historyManager.add(task);
    }

    public static String historyToString(HistoryManager manager) {
        StringBuilder hist = new StringBuilder();

        for (Task his : manager.getHistory()) {
            hist.append(his.getId()).append(",");
        }
        return hist.toString();
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        String[] content = null;
        Map<Long, Task> taskMap = new HashMap<>();
        try {
            content = Files.readString(file.toPath(), StandardCharsets.UTF_8).split("\n");
        } catch (IOException io) {
            throw new ManagerLoaderException("Нет файлов для чтения");
        }
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        for (int k = 1; k < content.length; k++) {
            if (content[k].isEmpty()) {
                int j = ++k;
                List<Long> historyFromString = historyFromString(content[j]);
                for (Long id : historyFromString) {
                    fileBackedTasksManager.saveToHistory(taskMap.get(id));
                }
                return fileBackedTasksManager;
            }

            Task task = fromString(content[k]);
            taskMap.put(task.getId(), task);
            if (task instanceof EpicTask) {
                fileBackedTasksManager.epicStorage.saveEpicTask((EpicTask) task);
//                fileBackedTasksManager.createEpicTask((EpicTask) task);
            } else if (task instanceof Subtask) {
                fileBackedTasksManager.subTaskStorage.putSubtask((Subtask) task);
//                fileBackedTasksManager.addSubtaskInEpicTask((Subtask) task);
            } else {
                fileBackedTasksManager.taskStorage.saveTask(task);
//                fileBackedTasksManager.createTask(task);
            }
            if (task.getId() > fileBackedTasksManager.idTask){
                fileBackedTasksManager.idTask = task.getId();
            }
        }
        return fileBackedTasksManager;
    }

    private String toString(Task task) {
        if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            return String.join(",", String.valueOf(subtask.getId()), TypesTasks.SUBTASK.toString(),
                    subtask.getName(), subtask.getStatus().toString(), subtask.getText(),
                    String.valueOf(subtask.getEpicId()));
        } else if (task instanceof EpicTask) {
            EpicTask epicTask = (EpicTask) task;
            return String.join(",", String.valueOf(epicTask.getId()), TypesTasks.EPIC.toString(),
                    epicTask.getName(), epicTask.getStatus().toString(), epicTask.getText());
        }

        return String.join(",", String.valueOf(task.getId()), TypesTasks.TASK.toString(), task.getName(),
                task.getStatus().toString(), task.getText());
    }

    public static Task fromString(String task) {
        String[] splitTask = task.split(",");

        switch (TypesTasks.valueOf(splitTask[1])) {
            case TASK:
                Task task1 = new Task(splitTask[2], splitTask[4], Status.valueOf(splitTask[3]));
                task1.setId(Long.parseLong(splitTask[0]));
                return task1;
            case EPIC:
                EpicTask epicTask = new EpicTask(splitTask[2], splitTask[4], Status.valueOf(splitTask[3]));
                epicTask.setId(Long.parseLong(splitTask[0]));
                return epicTask;
            case SUBTASK:
                Subtask subtask = new Subtask(Long.parseLong(splitTask[5]), splitTask[2], splitTask[4],
                        Status.valueOf(splitTask[3]));
                subtask.setId(Long.parseLong(splitTask[0]));
                return subtask;
        }
        throw new RuntimeException("В функцию передан неизвестный тип задачи");
    }

    public void save() {
        StringBuilder saveString = new StringBuilder("id,type,name,status,description,epic\n");
        for (Task task : getTaskList()) {
            saveString.append(toString(task)).append("\n");
        }

        for (Task task : getEpicTaskList()) {
            saveString.append(toString(task)).append("\n");
        }

        for (Task task : getSubtaskList()) {
            saveString.append(toString(task)).append("\n");
        }
        saveString.append("\n").append(historyToString(historyManager));
        Path path = file.toPath();
        try {
            if (Files.exists(path)) {
                Files.delete(path);
            }
            Files.createFile(path);
            Files.writeString(path, saveString, StandardCharsets.UTF_8);
        } catch (IOException io) {
            throw new ManagerSaveException("Ошибка при создании или записи файла");
        }
    }

    @Override
    public Task createTask(Task task) {
        super.createTask(task);
        save();

        return task;
    }

    @Override
    public EpicTask createEpicTask(EpicTask task) {
        super.createEpicTask(task);
        save();

        return task;
    }

    @Override
    public Subtask addSubtaskInEpicTask(Subtask subtask) {
        Subtask subtask1 = super.addSubtaskInEpicTask(subtask);
        save();
        return subtask1;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteTaskById(long id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteAllEpicTasks() {
        super.deleteAllEpicTasks();
        save();
    }

    @Override
    public void updateEpicTask(EpicTask task) {
        super.updateEpicTask(task);
        save();
    }

    @Override
    public void deleteEpicTaskById(Long id) {
        super.deleteEpicTaskById(id);
        save();
    }

    @Override
    public List<Subtask> getSubtaskListByEpicTaskId(long idEpicTask) {
        List<Subtask> subtasks = super.getSubtaskListByEpicTaskId(idEpicTask);
        save();
        return subtasks;

    }

    @Override
    public Subtask getSubTaskById(long id) {
        Subtask subtask = super.getSubTaskById(id);
        save();
        return subtask;
    }

    @Override
    public void deleteSubtaskById(long idTask) {
        super.deleteSubtaskById(idTask);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteAllSubtask() {
        super.deleteAllSubtask();
        save();
    }

    @Override
    public Task getTaskById(long id) {
        Task task = super.getTaskById(id);
        save();

        return task;
    }

    @Override
    public EpicTask getEpicTaskById(long id) {
        EpicTask epicTask = super.getEpicTaskById(id);
        save();

        return epicTask;
    }
}
