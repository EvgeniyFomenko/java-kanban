package ru.efomenko.service;

import ru.efomenko.model.EpicTask;
import ru.efomenko.model.Status;
import ru.efomenko.model.Subtask;
import ru.efomenko.model.Task;
import ru.efomenko.storage.EpicStorage;
import ru.efomenko.storage.SubTaskStorage;
import ru.efomenko.storage.TaskStorage;
import ru.efomenko.utility.Managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private EpicStorage epicStorage;
    private TaskStorage taskStorage;
    private SubTaskStorage subTaskStorage;
    private long idTask;
    protected final HistoryManager historyManager;


    public InMemoryTaskManager()  {
        epicStorage = new EpicStorage();
        taskStorage = new TaskStorage();
        subTaskStorage = new SubTaskStorage();
        historyManager = Managers.getDefaultHistoryManager();

    }

    @Override
    public List<Task> getTaskList() {
        return new ArrayList<>(taskStorage.getTaskHashMap().values());
    }

    @Override
    public void deleteAllTasks() {
        Map<Long, Task> taskMap = taskStorage.getTaskHashMap();
        for (Long id : taskMap.keySet()){
            historyManager.remove(id);
        }
        taskStorage = new TaskStorage();
    }

    @Override
    public Task getTaskById(long id) {
        Task task = taskStorage.getTaskById(id);
        historyManager.add(task);
        return taskStorage.getTaskById(id);
    }

    @Override
    public Task createTask(Task task) {
        idTask++;
        task.setId(idTask);
        taskStorage.saveTask(task);
        return task;
    }

    @Override
    public void updateTask(Task task) {
        taskStorage.saveTask(task);
    }

    @Override
    public void deleteTaskById(long id) {
        taskStorage.deleteTaskById(id);
        historyManager.remove(id);
    }

    @Override
    public List<EpicTask> getEpicTaskList() {
        return new ArrayList<>(epicStorage.getEpicTaskHashMap().values());
    }

    @Override
    public void deleteAllEpicTasks() {
        Map<Long, EpicTask> epicTaskMap = epicStorage.getEpicTaskHashMap();
        for (Long id : epicTaskMap.keySet()){
            historyManager.remove(id);
        }
        epicStorage = new EpicStorage();

        deleteAllSubtask();
    }

    @Override
    public EpicTask getEpicTaskById(long id) {
        EpicTask task = epicStorage.getEpicTaskHashMap().get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public EpicTask createEpicTask(EpicTask task) {
        idTask++;
        task.setId(idTask);
        setUpEpicStatus(task);
        epicStorage.saveEpicTask(task);
        return task;
    }

    @Override
    public void updateEpicTask(EpicTask task) {
        setUpEpicStatus(task);
        epicStorage.saveEpicTask(task);
    }

    @Override
    public void deleteEpicTaskById(Long id) {
        for (Long idSubtask : getEpicTaskById(id).getSubTaskIdList()) {
            subTaskStorage.deleteSubtaskById(idSubtask);
            historyManager.remove(idSubtask);
        }
        epicStorage.deleteEpicTaskById(id);
        historyManager.remove(id);
    }

    private void setUpEpicStatus(EpicTask epicTask) {
        if (epicTask.getSubTaskIdList().isEmpty()) {
            epicTask.setStatus(Status.NEW);
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
            epicTask.setStatus(Status.NEW);
        } else if (numbNew == 0) {
            epicTask.setStatus(Status.DONE);
        } else {
            epicTask.setStatus(Status.IN_PROGRES);
        }
    }

    @Override
    public List<Subtask> getSubtaskListByEpicTaskId(long idEpicTask) {
        List<Subtask> subtaskList = new ArrayList<>();

        for (long subtask : epicStorage.getEpicTaskById(idEpicTask).getSubTaskIdList()) {
            subtaskList.add(getSubTaskById(subtask));
        }
        return subtaskList;
    }

    public Subtask getSubTaskById(long id){
        Subtask task = subTaskStorage.getSubTaskById(id);
        historyManager.add(task);
      return task;
    }

    @Override
    public void deleteSubtaskById(long idTask) {
        EpicTask epicTask = getEpicTaskById(getSubTaskById(idTask).getEpicId());
        epicTask.deleteSubtaskById(idTask);
        subTaskStorage.deleteSubtaskById(idTask);
        setUpEpicStatus(epicTask);
        historyManager.remove(idTask);
    }

    @Override
    public void addSubtaskInEpicTask(Subtask subtask) {
        idTask++;
        subtask.setId(idTask);
        EpicTask epicTask = epicStorage.getEpicTaskHashMap().get(subtask.getEpicId());
        epicTask.addSubTaskId(idTask);
        subTaskStorage.putSubtask(subtask);
        updateEpicTask(epicTask);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subTaskStorage.putSubtask(subtask);
        updateEpicTask(epicStorage.getEpicTaskById(subtask.getEpicId()));
    }

    @Override
    public List<Subtask> getSubtaskList() {
        return new ArrayList<>(subTaskStorage.getSubtaskHashMap().values());
    }

    @Override
    public void deleteAllSubtask() {
        subTaskStorage = new SubTaskStorage();
        for (EpicTask epicTask : epicStorage.getEpicTaskHashMap().values()){
            epicTask.deleteAllSubtask();
            for (Long subtask : epicTask.getSubTaskIdList()){
                historyManager.remove(subtask);
            }
            setUpEpicStatus(epicTask);
        }
    }

    @Override
    public List<Task> getHistory(){
       return historyManager.getHistory();
    }

}
