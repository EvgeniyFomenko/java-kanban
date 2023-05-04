package ru.efomenko;

import ru.efomenko.model.EpicTask;
import ru.efomenko.model.Status;
import ru.efomenko.model.Subtask;
import ru.efomenko.service.InMemoryTaskManager;
import ru.efomenko.utility.Managers;

public class inMemoryHistoryManagerTest {
    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager ;
        inMemoryTaskManager =(InMemoryTaskManager) Managers.getDefault();

//        Task task = new Task("Сходить в магазин","В магазине купить продукты", Status.NEW);
        EpicTask epic = new EpicTask("Написать программу Канбан","Написать программу которая сохраняет " +
                "задачи и их делит на категории", Status.NEW);
        EpicTask epic1 = new EpicTask("Написать класс менеджер","В классе менеджер будет сосредоточена " +
                "вся работа с задачами",Status.NEW);

//        inMemoryTaskManager.createTask(task);
        long epic1Id = inMemoryTaskManager.createEpicTask(epic1).getId();
        long epicId = inMemoryTaskManager.createEpicTask(epic).getId();
        System.out.println("Печатаю истроию \n"+inMemoryTaskManager.getHistory().size()+" "+inMemoryTaskManager.getHistory().toString());
//        Subtask subtask = new Subtask(epicId,"Составить таблицу классов","",Status.NEW);
        Subtask subtask1 = new Subtask(epic1Id,"Составить список методов","",Status.DONE);
        Subtask subtask2 = new Subtask(epic1Id,"Запрограммировать методы","",Status.NEW);
        inMemoryTaskManager.addSubtaskInEpicTask(subtask1);
        System.out.println("Печатаю истроию \n"+inMemoryTaskManager.getHistory().size()+" "+inMemoryTaskManager.getHistory().toString());
        inMemoryTaskManager.addSubtaskInEpicTask(subtask2);
        System.out.println("Печатаю истроию \n"+inMemoryTaskManager.getHistory().size()+" "+inMemoryTaskManager.getHistory().toString());
//        inMemoryTaskManager.addSubtaskInEpicTask(subtask);
        inMemoryTaskManager.getEpicTaskById(2);
        inMemoryTaskManager.getEpicTaskById(1);
        System.out.println("Печатаю истроию \n"+inMemoryTaskManager.getHistory().size()+" "+inMemoryTaskManager.getHistory().toString());
        inMemoryTaskManager.getEpicTaskById(2);
        inMemoryTaskManager.getEpicTaskById(1);
        inMemoryTaskManager.deleteSubtaskById(3);
        inMemoryTaskManager.deleteSubtaskById(4);
        inMemoryTaskManager.deleteTaskById(1);
        inMemoryTaskManager.deleteTaskById(2);
        System.out.println("---------------------------------------------------------------------");

        inMemoryTaskManager.deleteAllSubtask();

        System.out.println("Печатаю истроию \n"+inMemoryTaskManager.getHistory().size()+" "+inMemoryTaskManager.getHistory().toString());
    }
}