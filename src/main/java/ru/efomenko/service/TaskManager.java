package ru.efomenko.service;

import ru.efomenko.model.EpicTask;
import ru.efomenko.model.Subtask;
import ru.efomenko.model.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getTaskList();

    void deleteAllTasks();

    Task getTaskById(long id);

    Task createTask(Task task);

    void updateTask(Task task);

    void deleteTaskById(long id);

    List<EpicTask> getEpicTaskList();

    void deleteAllEpicTasks();

    EpicTask getEpicTaskById(long id);

    EpicTask createEpicTask(EpicTask task);

    void updateEpicTask(EpicTask task);

    void deleteEpicTaskById(Long id);

    List<Subtask> getSubtaskListByEpicTaskId(long idEpicTask);

    void deleteSubtaskById(long idTask);

    void addSubtaskInEpicTask(Subtask subtask);

    void updateSubtask(Subtask subtask);

    List<Subtask> getSubtaskList();

    void deleteAllSubtask();

    List<Task> getHistory();
}
