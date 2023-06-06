package ru.efomenko.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.efomenko.model.EpicTask;
import ru.efomenko.model.Status;
import ru.efomenko.model.Subtask;
import ru.efomenko.model.Task;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static java.util.Calendar.FEBRUARY;
import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    T taskManager;
    @BeforeEach
    public void createObjects(){
    }

    @Test
    public void shouldReturnTaskManagerListEmptyList(){
        assertEquals(Collections.EMPTY_LIST,taskManager.getTaskList(),"Список не пуст");
    }

    @Test
    public void addNewTask(){
        Task task = new Task("Test task","description", Status.NEW);

        final long taskId = taskManager.createTask(task).getId();

        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTaskList();

        assertNotNull(tasks, "Задачи нe возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void addNewEpicTask(){
        EpicTask epicTask = new EpicTask("Тест епик таск","описание", Status.NEW);
        final EpicTask epicTask1 = taskManager.createEpicTask(epicTask);
        final long epicId = epicTask1.getId();

        final EpicTask savedTask = taskManager.getEpicTaskById(epicId);

        assertEquals(0,taskManager.getSubtaskListByEpicTaskId(epicId).size(),"В эпике содержатся " +
                "сабтаски которых не должно быть");
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(epicTask1, savedTask, "Задачи не совпадают.");

        final List<EpicTask> tasks = taskManager.getEpicTaskList();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(epicTask1, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void addNewSubTask(){
        EpicTask epicTask = new EpicTask("Тест епик таск","описание", Status.NEW);
        final long epicId = taskManager.createEpicTask(epicTask).getId();
        Subtask task = new Subtask(epicId,"Test task","description", Status.NEW);
        Subtask task1 = new Subtask(5,"Test task","description", Status.NEW);
        Subtask subtask = taskManager.addSubtaskInEpicTask(task1);
        assertNull(subtask);
        final long subtaskId = taskManager.addSubtaskInEpicTask(task).getId();

        final Task savedTask = taskManager.getSubTaskById(subtaskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Subtask> tasks = taskManager.getSubtaskList();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }
    @Test
    public void deleteTask(){
        Task task = new Task("Test task","description", Status.NEW);
        final long taskId = taskManager.createTask(task).getId();

        taskManager.deleteTaskById(taskId);

        assertNull(taskManager.getTaskById(taskId), "Задача найдена.");

        taskManager.createTask(task);
        taskManager.deleteAllTasks();
        assertEquals(Collections.EMPTY_LIST,taskManager.getTaskList(),"Задачи не были удалены");

    }

    @Test
    public void deleteEpicTask(){
        EpicTask task = new EpicTask("Test task","description", Status.NEW);
        final long taskId = taskManager.createEpicTask(task).getId();

        taskManager.deleteEpicTaskById(taskId);

        assertThrows(IllegalArgumentException.class,()->taskManager.getEpicTaskById(taskId));

        taskManager.createEpicTask(task);
        taskManager.deleteAllEpicTasks();
        assertEquals(Collections.EMPTY_LIST,taskManager.getEpicTaskList(),"Задачи не были удалены");

        taskManager.createEpicTask(task);

        Subtask subtask = new Subtask(taskId,"Subtask","Description",Status.NEW);

        long taskId1 = taskManager.createEpicTask(task).getId();
        Subtask subtask1 = new Subtask(taskId1,"Subtask","Description",Status.NEW);
        taskManager.addSubtaskInEpicTask(subtask1);

        taskManager.deleteAllEpicTasks();

        assertEquals(Collections.EMPTY_LIST,taskManager.getSubtaskList(),"При удалении епика не были" +
                " удаленые его сабтаски");
    }

    @Test
    public void deleteSubtask(){
        EpicTask epicTask = new EpicTask("Epic", "sdfsd",Status.NEW);
        long epicId = taskManager.createEpicTask(epicTask).getId();
        Subtask subtask = new Subtask(epicId,"Subtask","discription",Status.NEW);
        long subtaskId = taskManager.addSubtaskInEpicTask(subtask).getId();

        taskManager.deleteSubtaskById(subtaskId);
        int subtaskListSize = taskManager.getSubtaskList().size();
        assertEquals(0,subtaskListSize,"Subtask не удалился");
        assertThrows(IllegalArgumentException.class,()->taskManager.deleteSubtaskById(55));


        EpicTask epicTask1 = new EpicTask("Epic", "sdfsd",Status.NEW);
        long epicId1 = taskManager.createEpicTask(epicTask).getId();
        Subtask subtask1 = new Subtask(epicId,"Subtask","discription",Status.NEW);
         taskManager.addSubtaskInEpicTask(subtask);

        taskManager.deleteAllSubtask();
        assertEquals(0,subtaskListSize,"Subtasks не удалилилсь");
    }

    @Test
    public void updateTasks(){
        Task task = new Task("Task","Description",Status.NEW);
        EpicTask epicTask = new EpicTask("Epictask", "Description",Status.NEW);
        long epicId = taskManager.createEpicTask(epicTask).getId();
        Subtask subtask = new Subtask(epicId,"subtask","description",Status.NEW);
        long subtaskId = taskManager.addSubtaskInEpicTask(subtask).getId();
        long taskId = taskManager.createTask(task).getId();


        Task changeTask = new Task("Task1","Description",Status.NEW);
        changeTask.setId(taskId);

        EpicTask changeEpic = new EpicTask("Epictask1", "Description",Status.NEW);
        changeEpic.setId(epicId);

        Subtask changeSubtask = new Subtask(epicId,"subtask1","description",Status.NEW);
        changeSubtask.setId(subtaskId);

        taskManager.updateSubtask(changeSubtask);
        assertEquals(changeSubtask,taskManager.getSubTaskById(subtaskId),"Subtask не изменилась");

        taskManager.updateEpicTask(changeEpic);
        assertEquals(changeEpic,taskManager.getEpicTaskById(epicId),"Epic не изменилась");

        taskManager.updateTask(changeTask);
        assertEquals(changeTask,taskManager.getTaskById(taskId),"Task не изменилась");
    }

    @Test
    public void history(){
        Task task = new Task("Task","Description",Status.NEW);
        EpicTask epicTask = new EpicTask("Epictask", "Description",Status.NEW);
        long epicId = taskManager.createEpicTask(epicTask).getId();
        Subtask subtask = new Subtask(epicId,"subtask","description",Status.NEW);
        long subtaskId = taskManager.addSubtaskInEpicTask(subtask).getId();
        long taskId = taskManager.createTask(task).getId();

        taskManager.getSubTaskById(subtaskId);
        taskManager.getEpicTaskById(epicId);
        taskManager.getTaskById(taskId);

        assertEquals(3, taskManager.getHistory().size(),"История отображается не верно");
    }

    @Test
    void getSortTaskList(){
        LocalDateTime dateTimeOfTwos = LocalDateTime.of(2023, FEBRUARY, 2, 22, 22);
        Task task = new Task("Test task","description", Status.NEW,dateTimeOfTwos,10);
        Task task1 = new Task("Test task","description", Status.NEW);
        taskManager.createTask(task1);
        final long taskId = taskManager.createTask(task).getId();
        EpicTask epic1 = new EpicTask("Epic","Discr",Status.NEW);
        long epicId = taskManager.createEpicTask(epic1).getId();
        Subtask subtask1 = new Subtask(epicId,"Составить список методов","description",Status.NEW,
                dateTimeOfTwos.plusHours(1),30);
        Subtask subtask2 = new Subtask(epicId,"Запрограммировать методы","description",Status.DONE,
                dateTimeOfTwos.plusHours(2),10);
        Subtask subtask = new Subtask(epicId,"Составить таблицу классов","",Status.IN_PROGRES,
                dateTimeOfTwos.plusHours(3),10);

        taskManager.addSubtaskInEpicTask(subtask);
        taskManager.addSubtaskInEpicTask(subtask1);
        taskManager.addSubtaskInEpicTask(subtask2);

        List<Task> taskList = taskManager.getPrioritizedTasks();

        assertNotNull(taskList);
    }

    @Test
    void validationTime(){
        LocalDateTime dateTimeOfTwos = LocalDateTime.of(2023, FEBRUARY, 2, 22, 22);
        Task task = new Task("Task","Discription",Status.NEW,dateTimeOfTwos,5);
        Task task1 = new Task ("Task1","Description1",Status.NEW,dateTimeOfTwos,10);
        taskManager.createTask(task);
        assertThrows(IllegalArgumentException.class,()->{taskManager.createTask(task1);});

        Task task2 = new Task("Task2","Description2",Status.NEW,dateTimeOfTwos.plusHours(1),5);
        long taskid = taskManager.createTask(task2).getId();
        int sizeTasks = taskManager.getTaskList().size();

        assertEquals(2,sizeTasks);
        Task taskSave = new Task("Task2","Description2",Status.NEW,dateTimeOfTwos,5);
        taskSave.setId(taskid);

        assertThrows(IllegalArgumentException.class,()->taskManager.updateTask(taskSave));

        EpicTask epicTask = new EpicTask("Epic","Descr",Status.NEW);
        long epicId = taskManager.createEpicTask(epicTask).getId();
        Subtask subtask = new Subtask(epicId,"Subtask","Descr",Status.NEW,dateTimeOfTwos,10);
        assertThrows(IllegalArgumentException.class,()->taskManager.addSubtaskInEpicTask(subtask));


    }
}
