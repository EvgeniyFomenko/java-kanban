package ru.efomenko.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.efomenko.model.EpicTask;
import ru.efomenko.model.Status;
import ru.efomenko.model.Subtask;
import ru.efomenko.model.Task;
import ru.efomenko.utility.Managers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.Calendar.FEBRUARY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FileBackedTasksManagerTest extends TaskManagerTest {
    Path path = Path.of("resources/test.csv");
    File file = path.toFile();

    @BeforeEach
    @Override
    public void createObjects() {

        file.delete();
        try {
            file.createNewFile();
        } catch (IOException io) {
            System.out.println(io.getMessage());
        }
        super.taskManager = Managers.getFileBackedTaskManager(new File("resources/test.csv"));
    }

    @Test
    @Override
    public void shouldReturnTaskManagerListEmptyList() {
        super.shouldReturnTaskManagerListEmptyList();
    }

    @Test
    @Override
    public void addNewTask() {
        super.addNewTask();
    }

    @Test
    @Override
    public void addNewEpicTask() {
        super.addNewEpicTask();
    }

    @Test
    @Override
    public void addNewSubTask() {
        super.addNewSubTask();
    }

    @Test
    @Override
    public void deleteTask() {
        super.deleteTask();
    }

    @Test
    @Override
    public void deleteEpicTask() {
        super.deleteEpicTask();
    }

    @Test
    @Override
    public void deleteSubtask() {
        super.deleteSubtask();
    }

    @Test
    @Override
    public void updateTasks() {
        super.updateTasks();
    }

    @Test
    @Override
    public void history() {
        super.history();
    }

    @Test
    public void saveAndRecovery() {


        assertEquals(0, taskManager.getTaskList().size());
        assertEquals(0, taskManager.getHistory().size());


        EpicTask epicTask = new EpicTask("Тест епик таск", "описание", Status.NEW);
        final long epicId = taskManager.createEpicTask(epicTask).getId();
        Subtask task = new Subtask(epicId, "Test subtask", "description", Status.NEW);
        Task task1 = new Task("Test task", "description", Status.NEW);
        long subtaskId = taskManager.addSubtaskInEpicTask(task).getId();
        final long taskId = taskManager.createTask(task1).getId();

        Task saveTask = taskManager.getTaskById(taskId);
        EpicTask saveEpic = taskManager.getEpicTaskById(epicId);
        Subtask saveSubtask = taskManager.getSubTaskById(subtaskId);

        super.taskManager = Managers.getFileBackedTaskManager(new File("resources/test.csv"));

        assertEquals(1, taskManager.getTaskList().size());
        assertEquals(1, taskManager.getEpicTaskList().size());
        assertEquals(1, taskManager.getSubtaskList().size());

        assertEquals(saveTask, taskManager.getTaskById(taskId));
        assertEquals(saveEpic, taskManager.getEpicTaskById(epicId));
        assertEquals(saveSubtask, taskManager.getSubTaskById(subtaskId));
        assertEquals(3, taskManager.getHistory().size());
    }

    @Test
    public void recoveryEpicWithoutSubtask() {
        EpicTask epicTask = new EpicTask("Тест епик таск", "описание", Status.NEW);
        final long epicId = taskManager.createEpicTask(epicTask).getId();

        super.taskManager = Managers.getFileBackedTaskManager(new File("resources/test.csv"));

        assertEquals(0, taskManager.getEpicTaskById(epicId).getSubTaskIdList().size());

    }

    @Test
    public void recoveryListWithoutHistory() {
        EpicTask epicTask = new EpicTask("Тест епик таск", "описание", Status.NEW);
        final long epicId = taskManager.createEpicTask(epicTask).getId();

        super.taskManager = Managers.getFileBackedTaskManager(new File("resources/test.csv"));

        assertEquals(0, taskManager.getHistory().size());
    }

    @Test
    public void saveAndRecoveryWithDate() {


        assertEquals(0, taskManager.getTaskList().size());
        assertEquals(0, taskManager.getHistory().size());

        EpicTask epic1 = new EpicTask("Epic", "Discr", Status.NEW);
        long epicId = taskManager.createEpicTask(epic1).getId();
        LocalDateTime dateTimeOfTwos = LocalDateTime.of(2023, FEBRUARY, 2, 22, 22);
        Subtask subtask1 = new Subtask(epicId, "Составить список методов", "description", Status.NEW,
                dateTimeOfTwos, 30);
        Subtask subtask2 = new Subtask(epicId, "Запрограммировать методы", "description", Status.DONE,
                dateTimeOfTwos.plusHours(5), 10);
        Subtask subtask = new Subtask(epicId, "Составить таблицу классов", "", Status.IN_PROGRES,
                dateTimeOfTwos.plusHours(6), 10);

        taskManager.addSubtaskInEpicTask(subtask);
        taskManager.addSubtaskInEpicTask(subtask1);
        taskManager.addSubtaskInEpicTask(subtask2);
//        System.out.println(subtask.getEndTime()+"\n"+subtask1.getEndTime()+"\n"+subtask2.getEndTime());
//        System.out.println(taskManager.getEpicTaskById(epicId));

//        EpicTask epicTask = new EpicTask("Тест епик таск","описание", Status.NEW);
//        final long epicId = taskManager.createEpicTask(epicTask).getId();
//        Subtask task = new Subtask(epicId,"Test subtask","description", Status.NEW);
//        Task task1 = new Task("Test task","description", Status.NEW);
//        long subtaskId = taskManager.addSubtaskInEpicTask(task).getId();
//        final long taskId = taskManager.createTask(task1).getId();
//
//        Task saveTask = taskManager.getTaskById(taskId);
//        EpicTask saveEpic = taskManager.getEpicTaskById(epicId);
//        Subtask saveSubtask = taskManager.getSubTaskById(subtaskId);
//
//        super.taskManager = Managers.getFileBackedTaskManager(new File("resources/test.csv"));
//
//        assertEquals(1,taskManager.getTaskList().size());
//        assertEquals(1,taskManager.getEpicTaskList().size());
//        assertEquals(1,taskManager.getSubtaskList().size());
//
//        assertEquals(saveTask,taskManager.getTaskById(taskId));
//        assertEquals(saveEpic,taskManager.getEpicTaskById(epicId));
//        assertEquals(saveSubtask,taskManager.getSubTaskById(subtaskId));
//        assertEquals(3,taskManager.getHistory().size());
    }

    @Test
    public void readTaskWithDate() {
        LocalDateTime dateTimeOfTwos = LocalDateTime.of(2023, FEBRUARY, 2, 22, 22);
        Task task = new Task("Test task", "description", Status.NEW, dateTimeOfTwos, 10);
        final long taskId = taskManager.createTask(task).getId();
        EpicTask epic1 = new EpicTask("Epic", "Discr", Status.NEW);
        long epicId = taskManager.createEpicTask(epic1).getId();
        Subtask subtask1 = new Subtask(epicId, "Составить список методов", "description", Status.NEW,
                dateTimeOfTwos.plusHours(1), 30);
        Subtask subtask2 = new Subtask(epicId, "Запрограммировать методы", "description", Status.DONE,
                dateTimeOfTwos.plusHours(5), 10);
        Subtask subtask = new Subtask(epicId, "Составить таблицу классов", "", Status.IN_PROGRES,
                dateTimeOfTwos.plusHours(6), 10);

        taskManager.addSubtaskInEpicTask(subtask);
        taskManager.addSubtaskInEpicTask(subtask1);
        taskManager.addSubtaskInEpicTask(subtask2);

        super.taskManager = Managers.getFileBackedTaskManager(new File("resources/test.csv"));


        List<EpicTask> epicTaskList = taskManager.getEpicTaskList();
        assertNotNull(epicTaskList);

        List<Subtask> subtaskList = taskManager.getSubtaskList();
        assertNotNull(subtaskList);

        List<Task> taskList = taskManager.getTaskList();
        assertNotNull(taskList);
    }

    @Test
    @Override
    void getSortTaskList() {
        super.getSortTaskList();
    }

    @Test
    @Override
    void validationTime() {
        super.validationTime();
    }
}