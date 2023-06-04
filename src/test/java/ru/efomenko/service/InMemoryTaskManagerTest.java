package ru.efomenko.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.efomenko.utility.Managers;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void createObjects() {
        super.taskManager = (InMemoryTaskManager) Managers.getDefault();
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
    public void addNewEpicTask() {
        super.addNewEpicTask();
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
}