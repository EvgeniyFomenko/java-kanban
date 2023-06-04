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

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest extends TaskManagerTest {
    Path path = Path.of("resources/test.csv");
    File file = path.toFile();

    @BeforeEach
    @Override
    public void createObjects() {

        file.delete();
        try {
            file.createNewFile();
        }catch (IOException io){
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
    public void saveAndRecovery(){


        assertEquals(0,taskManager.getTaskList().size());
        assertEquals(0,taskManager.getHistory().size());


        EpicTask epicTask = new EpicTask("Тест епик таск","описание", Status.NEW);
        final long epicId = taskManager.createEpicTask(epicTask).getId();
        Subtask task = new Subtask(epicId,"Test subtask","description", Status.NEW);
        Task task1 = new Task("Test task","description", Status.NEW);
        long subtaskId = taskManager.addSubtaskInEpicTask(task).getId();
        final long taskId = taskManager.createTask(task1).getId();

        Task saveTask = taskManager.getTaskById(taskId);
        EpicTask saveEpic = taskManager.getEpicTaskById(epicId);
        Subtask saveSubtask = taskManager.getSubTaskById(subtaskId);

        super.taskManager = Managers.getFileBackedTaskManager(new File("resources/test.csv"));

        assertEquals(1,taskManager.getTaskList().size());
        assertEquals(1,taskManager.getEpicTaskList().size());
        assertEquals(1,taskManager.getSubtaskList().size());

        assertEquals(saveTask,taskManager.getTaskById(taskId));
        assertEquals(saveEpic,taskManager.getEpicTaskById(epicId));
        assertEquals(saveSubtask,taskManager.getSubTaskById(subtaskId));
        assertEquals(3,taskManager.getHistory().size());
    }

    @Test
    public void recoveryEpicWithoutSubtask(){
        EpicTask epicTask = new EpicTask("Тест епик таск","описание", Status.NEW);
        final long epicId = taskManager.createEpicTask(epicTask).getId();

        super.taskManager = Managers.getFileBackedTaskManager(new File("resources/test.csv"));

        assertEquals(0,taskManager.getEpicTaskById(epicId).getSubTaskIdList().size());

    }

    @Test
    public void recoveryListWithoutHistory(){
        EpicTask epicTask = new EpicTask("Тест епик таск","описание", Status.NEW);
        final long epicId = taskManager.createEpicTask(epicTask).getId();

        super.taskManager = Managers.getFileBackedTaskManager(new File("resources/test.csv"));

        assertEquals(0,taskManager.getHistory().size());
    }
}