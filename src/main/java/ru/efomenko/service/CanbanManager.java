package ru.efomenko.service;

import ru.efomenko.model.EpicTask;
import ru.efomenko.model.Status;
import ru.efomenko.model.Subtask;
import ru.efomenko.model.Task;
import ru.efomenko.storage.EpicStorage;
import ru.efomenko.storage.SubTaskStorage;
import ru.efomenko.storage.TaskStorage;

import java.util.ArrayList;
import java.util.List;

public class CanbanManager {
    private EpicStorage epicStorage;
    private TaskStorage taskStorage;
    private SubTaskStorage subTaskStorage;
    private long idTask;

    public CanbanManager() {
        epicStorage = new EpicStorage();
        taskStorage = new TaskStorage();
        subTaskStorage = new SubTaskStorage();
    }

    public List<Task> getTaskList() {
        return new ArrayList<>(taskStorage.getTaskHashMap().values());
    }

    public void deleteAllTasks() {
        taskStorage = new TaskStorage();
    }

    public Task getTaskById(long id) {
        return taskStorage.getTaskById(id);
    }

    public Task createTask(Task task) {
        idTask++;
        task.setId(idTask);
        taskStorage.saveTask(task);
        return task;
    }

    public void updateTask(Task task) {
        taskStorage.saveTask(task);
    }

    public void deleteTaskById(long id) {
        taskStorage.deleteTaskById(id);
    }

    public List<EpicTask> getEpicTaskList() {
        return new ArrayList<>(epicStorage.getEpicTaskHashMap().values());
    }

    public void deleteAllEpicTasks() {
        epicStorage = new EpicStorage();

        deleteAllSubtask();
    }

    public EpicTask getEpicTaskById(long id) {
        return epicStorage.getEpicTaskHashMap().get(id);
    }

    public EpicTask createEpicTask(EpicTask task) {
        idTask++;
        task.setId(idTask);
        setUpEpicStatus(task);
        epicStorage.saveEpicTask(task);
        return task;
    }

    public void updateEpicTask(EpicTask task) {
        setUpEpicStatus(task);
        epicStorage.saveEpicTask(task);
    }

    public void deleteEpicTaskById(Long id) {
        for (Long idSubtask : getEpicTaskById(id).getSubTaskIdList()) {
            deleteSubtaskById(idSubtask);
        }
        epicStorage.deleteEpicTaskById(id);
    }

    private void setUpEpicStatus(EpicTask epicTask) {
        if (epicTask.getSubTaskIdList().isEmpty()) {
            epicTask.setStatus(Status.STATUS.NEW);
            return;
        }
        List<Long> subTaskIdList = epicTask.getSubTaskIdList();
        int numbNew = 0;
        int numbDone = 0;

        for (Long taskId : subTaskIdList) {
            Subtask task = subTaskStorage.getSubTaskById(taskId);

            switch (task.getStatus()) {
                case NEW:
                    numbNew++;
                    break;
                case DONE:
                    numbDone++;
                    break;
            }
        }

        if (numbDone == 0) {
            epicTask.setStatus(Status.STATUS.NEW);
        } else if (numbNew == 0) {
            epicTask.setStatus(Status.STATUS.DONE);
        } else {
            epicTask.setStatus(Status.STATUS.IN_PROGRES);
        }
    }

    public List<Subtask> getSubtaskListByEpicTaskId(long idEpicTask) {
        List<Subtask> subtaskList = new ArrayList<>();

        for (long subtask : epicStorage.getEpicTaskById(idEpicTask).getSubTaskIdList()) {
            subtaskList.add(subTaskStorage.getSubTaskById(subtask));
        }
        return subtaskList;
    }

    public void deleteSubtaskById(long idTask) {
        EpicTask epicTask = getEpicTaskById(subTaskStorage.getSubTaskById(idTask).getEpicId());
        epicTask.deleteSubtaskById(idTask);
        subTaskStorage.deleteSubtaskById(idTask);
        setUpEpicStatus(epicTask);
    }

    public void addSubtaskInEpicTask(Subtask subtask) {
        idTask++;
        subtask.setId(idTask);
        EpicTask epicTask = getEpicTaskById(subtask.getEpicId());
        epicTask.addSubTaskId(idTask);
        subTaskStorage.putSubtask(subtask);
        updateEpicTask(epicTask);
    }

    public void updateSubtask(Subtask subtask) {
        subTaskStorage.putSubtask(subtask);
        updateEpicTask(epicStorage.getEpicTaskById(subtask.getEpicId()));
    }

    public List<Subtask> getSubtaskList() {
        return new ArrayList<>(subTaskStorage.getSubtaskHashMap().values());
    }

    public void deleteAllSubtask() {
        subTaskStorage = new SubTaskStorage();
        for (EpicTask epicTask : epicStorage.getEpicTaskHashMap().values()){
            epicTask.deleteAllSubtask();
            setUpEpicStatus(epicTask);
        }
    }

}
