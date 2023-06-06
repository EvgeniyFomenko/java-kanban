package ru.efomenko.service;

import ru.efomenko.exceptions.TasksTimeValidationException;
import ru.efomenko.model.EpicTask;
import ru.efomenko.model.Status;
import ru.efomenko.model.Subtask;
import ru.efomenko.model.Task;
import ru.efomenko.storage.EpicStorage;
import ru.efomenko.storage.SubTaskStorage;
import ru.efomenko.storage.TaskStorage;
import ru.efomenko.utility.Managers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected EpicStorage epicStorage;
    protected TaskStorage taskStorage;
    protected SubTaskStorage subTaskStorage;
    protected long idTask;
    protected final HistoryManager historyManager;
    protected TreeSet<Task> sortedTasks;


    public InMemoryTaskManager() {
        epicStorage = new EpicStorage();
        taskStorage = new TaskStorage();
        subTaskStorage = new SubTaskStorage();
        sortedTasks = new TreeSet<>((e, e1) -> {
            if (e.getStartTime() == null) {
                return 1;
            } else if (e1.getStartTime() == null) {
                return -1;
            } else if (e.getStartTime().isAfter(e1.getStartTime())) {
                return 1;
            } else if (e.getStartTime().isBefore(e1.getStartTime())) {
                return -1;
            } else {
                return 0;
            }
        });
        historyManager = Managers.getDefaultHistoryManager();

    }

    @Override
    public List<Task> getTaskList() {
        return new ArrayList<>(taskStorage.getTaskHashMap().values());
    }

    @Override
    public void deleteAllTasks() {
        getTaskList().forEach(sortedTasks::remove);
        Map<Long, Task> taskMap = taskStorage.getTaskHashMap();
        for (Long id : taskMap.keySet()) {
            historyManager.remove(id);
        }
        taskStorage = new TaskStorage();
    }

    @Override
    public Task getTaskById(long id) {
        Task task = taskStorage.getTaskById(id);
        if (task == null) {
            return null;
        }
        historyManager.add(task);
        return taskStorage.getTaskById(id);
    }

    @Override
    public Task createTask(Task task) {
        if(!Objects.isNull(task.getStartTime())) {
            if ( validationTime(task)){
                throw new TasksTimeValidationException("Задача с таким временем уже существует");
            }
        }
        idTask++;
        task.setId(idTask);

        sortedTasks.add(task);
        taskStorage.saveTask(task);
        return task;
    }

    @Override
    public void updateTask(Task task) {
        if (task.getStartTime() != null & validationTime(task)) {
            throw new TasksTimeValidationException("Задача с таким временем уже существует");
        }
        sortedTasks.remove(taskStorage.getTaskById(task.getId()));
        sortedTasks.add(task);
        taskStorage.saveTask(task);
    }

    @Override
    public void deleteTaskById(long id) {
        sortedTasks.remove(taskStorage.getTaskById(id));
        taskStorage.deleteTaskById(id);
        historyManager.remove(id);
    }

    @Override
    public List<EpicTask> getEpicTaskList() {
        return new ArrayList<>(epicStorage.getEpicTaskHashMap().values());
    }

    @Override
    public void deleteAllEpicTasks() {
        getEpicTaskList().forEach(sortedTasks::remove);
        Map<Long, EpicTask> epicTaskMap = epicStorage.getEpicTaskHashMap();
        for (Long id : epicTaskMap.keySet()) {
            historyManager.remove(id);
        }
        epicStorage = new EpicStorage();

        deleteAllSubtask();
    }

    @Override
    public EpicTask getEpicTaskById(long id) {
        EpicTask task = epicStorage.getEpicTaskHashMap().get(id);
        if (task == null) {
            throw new IllegalArgumentException("EpicTask с таким id не существует");
        }
        historyManager.add(task);
        return task;
    }

    @Override
    public EpicTask createEpicTask(EpicTask task) {
        idTask++;
        task.setId(idTask);
        setUpEpicStatus(task);
        sortedTasks.add(task);
        epicStorage.saveEpicTask(task);
        return task;
    }

    @Override
    public void updateEpicTask(EpicTask task) {
        setUpEpicStatus(task);
        sortedTasks.remove(epicStorage.getEpicTaskById(task.getId()));
        sortedTasks.add(task);
        epicStorage.saveEpicTask(task);
    }

    @Override
    public void deleteEpicTaskById(Long id) {
        for (Long idSubtask : getEpicTaskById(id).getSubTaskIdList()) {
            subTaskStorage.deleteSubtaskById(idSubtask);
            historyManager.remove(idSubtask);
        }
        sortedTasks.remove(epicStorage.getEpicTaskById(id));
        epicStorage.deleteEpicTaskById(id);
        historyManager.remove(id);
    }

    private void setUpEpicEndTime(EpicTask epicTask) {

        List<Long> subTaskIdList = epicTask.getSubTaskIdList();

        LocalDateTime highDate = null;
        LocalDateTime lowerDate = null;
        Duration duration = Duration.ofMinutes(0);

        for (Long taskId : subTaskIdList) {
            Subtask task = subTaskStorage.getSubTaskById(taskId);
            if (task.getStartTime() == null) {
                continue;
            }
            duration = duration.plus(task.getDuration());


            if (highDate == null) {
                highDate = task.getStartTime();
            }

            if (lowerDate == null) {
                lowerDate = task.getEndTime();
            }

            if (highDate.isAfter(task.getStartTime())) {
                highDate = task.getStartTime();
            }

            if (lowerDate.isBefore(task.getEndTime())) {
                System.out.println(task.getEndTime());
                lowerDate = task.getEndTime();
            }
        }
        epicTask.setDuration(duration.toMinutes());
        epicTask.setStartTime(highDate);
        epicTask.setEndTime(lowerDate);


    }

    private void setUpEpicStatus(EpicTask epicTask) {
        if (epicTask.getSubTaskIdList().isEmpty()) {
            epicTask.setStatus(Status.NEW);
            return;
        }
        setUpEpicEndTime(epicTask);

        List<Long> subTaskIdList = epicTask.getSubTaskIdList();
        int numbNew = 0;
        int numbDone = 0;
        int numbInProgress = 0;

        for (Long taskId : subTaskIdList) {
            Subtask task = subTaskStorage.getSubTaskById(taskId);
            switch (task.getStatus()) {
                case NEW:
                    numbNew++;
                    break;
                case DONE:
                    numbDone++;
                    break;
                case IN_PROGRES:
                    numbInProgress++;
                    break;
            }
        }

        if (numbDone == 0 && numbInProgress == 0) {
            epicTask.setStatus(Status.NEW);
        } else if (numbNew == 0 && numbInProgress == 0) {
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

    @Override
    public Subtask getSubTaskById(long id) {
        Subtask task = subTaskStorage.getSubTaskById(id);
        if (task == null) {
            return null;
        }
        historyManager.add(task);
        return task;
    }

    @Override
    public void deleteSubtaskById(long idTask) {
        Subtask subtask = getSubTaskById(idTask);
        if (subtask == null) {
           return;
        }
        EpicTask epicTask = getEpicTaskById(subtask.getEpicId());
        if (epicTask == null) {
            return;
        }
        epicTask.deleteSubtaskById(idTask);
        sortedTasks.remove(subTaskStorage.getSubTaskById(idTask));
        subTaskStorage.deleteSubtaskById(idTask);
        setUpEpicStatus(epicTask);
        historyManager.remove(idTask);
    }

    @Override
    public Subtask addSubtaskInEpicTask(Subtask subtask) {
        if (subtask.getStartTime() != null) {
            if (validationTime(subtask)) {
                throw new TasksTimeValidationException("Задача с таким временем уже существует");
            }
        }
        idTask++;
        subtask.setId(idTask);
        EpicTask epicTask = epicStorage.getEpicTaskHashMap().get(subtask.getEpicId());
        if (epicTask == null) {
            return null;
        }
        epicTask.addSubTaskId(idTask);
        sortedTasks.add(subtask);
        subTaskStorage.putSubtask(subtask);
        updateEpicTask(epicTask);
        return subtask;
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subTaskStorage.putSubtask(subtask);
        sortedTasks.add(subtask);
        updateEpicTask(epicStorage.getEpicTaskById(subtask.getEpicId()));
    }

    @Override
    public List<Subtask> getSubtaskList() {
        return new ArrayList<>(subTaskStorage.getSubtaskHashMap().values());
    }

    @Override
    public void deleteAllSubtask() {
        getSubtaskList().forEach(sortedTasks::remove);
        subTaskStorage = new SubTaskStorage();
        for (EpicTask epicTask : epicStorage.getEpicTaskHashMap().values()) {
            epicTask.deleteAllSubtask();
            for (long subtask : epicTask.getSubTaskIdList()) {
                historyManager.remove(subtask);
            }
            setUpEpicStatus(epicTask);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return Collections.list(Collections.enumeration(sortedTasks));
    }

    protected boolean validationTime(Task task) {
        return getPrioritizedTasks().stream().anyMatch((e) -> {
                    if (task instanceof EpicTask && e instanceof Subtask || e instanceof EpicTask && task instanceof Subtask) {
                        return false;
                    }
                    if (task.getId() == e.getId()) {
                        return false;
                    }
                    if (e.getStartTime() == null || task.getStartTime() == null) {
                        return false;
                    }
                    return e.getStartTime().isEqual(task.getStartTime()) || e.getStartTime().isAfter(task.getStartTime()) &&
                            e.getEndTime().isBefore(task.getEndTime()) || e.getEndTime().isEqual(task.getEndTime());
                }
        );
    }
}
