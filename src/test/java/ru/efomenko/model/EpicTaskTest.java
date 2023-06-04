package ru.efomenko.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.efomenko.service.InMemoryTaskManager;
import ru.efomenko.utility.Managers;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTaskTest {
    InMemoryTaskManager inMemoryTaskManager ;
    EpicTask epic;
    @BeforeEach
    public void createStartConstructor(){
        inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
        epic = new EpicTask("Написать программу Канбан","Написать программу которая сохраняет " +
                "задачи и их делит на категории", Status.NEW);
    }

    @Test
    public void shouldReturnEpicStatusNewWithoutSubtask(){
        long epicId = inMemoryTaskManager.createEpicTask(epic).getId();

        assertEquals(Status.NEW,inMemoryTaskManager.getEpicTaskById(epicId).getStatus());

    }

    @Test
    public void shouldReturnEpicStatusNewWithSubtasksStatusNew(){
        long epicId = inMemoryTaskManager.createEpicTask(epic).getId();
        Subtask subtask1 = new Subtask(epicId,"Составить список методов","description",Status.NEW);
        Subtask subtask2 = new Subtask(epicId,"Запрограммировать методы","description",Status.NEW);
        Subtask subtask = new Subtask(epicId,"Составить таблицу классов","",Status.NEW);

        inMemoryTaskManager.addSubtaskInEpicTask(subtask);
        inMemoryTaskManager.addSubtaskInEpicTask(subtask1);
        inMemoryTaskManager.addSubtaskInEpicTask(subtask2);



        assertEquals(Status.NEW,inMemoryTaskManager.getEpicTaskById(epicId).getStatus());
    }

    @Test
    public void shouldReturnEpicStatusDoneWithSubtasksStatusDone(){
        long epicId = inMemoryTaskManager.createEpicTask(epic).getId();
        Subtask subtask1 = new Subtask(epicId,"Составить список методов","description",Status.DONE);
        Subtask subtask2 = new Subtask(epicId,"Запрограммировать методы","description",Status.DONE);
        Subtask subtask = new Subtask(epicId,"Составить таблицу классов","",Status.DONE);

        inMemoryTaskManager.addSubtaskInEpicTask(subtask);
        inMemoryTaskManager.addSubtaskInEpicTask(subtask1);
        inMemoryTaskManager.addSubtaskInEpicTask(subtask2);



        assertEquals(Status.DONE,inMemoryTaskManager.getEpicTaskById(epicId).getStatus());
    }

    @Test
    public void shouldReturnEpicStatusDoneWithSubtasksStatusDoneAndNew(){
        long epicId = inMemoryTaskManager.createEpicTask(epic).getId();
        Subtask subtask1 = new Subtask(epicId,"Составить список методов","description",Status.NEW);
        Subtask subtask2 = new Subtask(epicId,"Запрограммировать методы","description",Status.DONE);
        Subtask subtask = new Subtask(epicId,"Составить таблицу классов","",Status.DONE);

        inMemoryTaskManager.addSubtaskInEpicTask(subtask);
        inMemoryTaskManager.addSubtaskInEpicTask(subtask1);
        inMemoryTaskManager.addSubtaskInEpicTask(subtask2);



        assertEquals(Status.IN_PROGRES,inMemoryTaskManager.getEpicTaskById(epicId).getStatus());
    }

    @Test
    public void shouldReturnEpicStatusDoneWithSubtasksStatusInProgress(){
        long epicId = inMemoryTaskManager.createEpicTask(epic).getId();
        Subtask subtask1 = new Subtask(epicId,"Составить список методов","description",Status.NEW);
        Subtask subtask2 = new Subtask(epicId,"Запрограммировать методы","description",Status.DONE);
        Subtask subtask = new Subtask(epicId,"Составить таблицу классов","",Status.IN_PROGRES);

        inMemoryTaskManager.addSubtaskInEpicTask(subtask);
        inMemoryTaskManager.addSubtaskInEpicTask(subtask1);
        inMemoryTaskManager.addSubtaskInEpicTask(subtask2);



        assertEquals(Status.IN_PROGRES,inMemoryTaskManager.getEpicTaskById(epicId).getStatus());
    }

}