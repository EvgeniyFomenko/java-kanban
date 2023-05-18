package ru.efomenko;

import ru.efomenko.model.EpicTask;
import ru.efomenko.model.Status;
import ru.efomenko.model.Subtask;
import ru.efomenko.model.Task;
import ru.efomenko.service.FileBackedTasksManager;
import ru.efomenko.service.InMemoryTaskManager;
import ru.efomenko.utility.Managers;

public class inMemoryHistoryManagerTest {
    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager ;
//        inMemoryTaskManager =(FileBackedTasksManager) FileBackedTasksManager.loadFromFile("rescources/fileSave.csv");
        inMemoryTaskManager = (FileBackedTasksManager) Managers.getFileBackedTaskManager();

        Task task = new Task("Сходить в магазин","В магазине купить продукты", Status.NEW);
        EpicTask epic = new EpicTask("Написать программу Канбан","Написать программу которая сохраняет " +
                "задачи и их делит на категории", Status.NEW);
        EpicTask epic1 = new EpicTask("Написать класс менеджер","В классе менеджер будет сосредоточена " +
                "вся работа с задачами",Status.NEW);

        long taskId = inMemoryTaskManager.createTask(task).getId();
        long epic1Id = inMemoryTaskManager.createEpicTask(epic1).getId();
        long epicId = inMemoryTaskManager.createEpicTask(epic).getId();

        Subtask subtask1 = new Subtask(epic1Id,"Составить список методов","description",Status.DONE);
        Subtask subtask2 = new Subtask(epic1Id,"Запрограммировать методы","description",Status.NEW);

        System.out.println("Печатаю истроию \n"+inMemoryTaskManager.getHistory().size()+" "+inMemoryTaskManager.getHistory().toString());
//        Subtask subtask = new Subtask(epicId,"Составить таблицу классов","",Status.NEW);

        inMemoryTaskManager.addSubtaskInEpicTask(subtask1);
        inMemoryTaskManager.addSubtaskInEpicTask(subtask2);

        System.out.println("Печатаю истроию \n"+inMemoryTaskManager.getHistory().size()+" "+inMemoryTaskManager.getHistory().toString());

    //        inMemoryTaskManager.addSubtaskInEpicTask(subtask);
//        inMemoryTaskManager.getEpicTaskById(epicId);
//        inMemoryTaskManager.getEpicTaskById(epic1Id);
//        inMemoryTaskManager.getTaskById(taskId);
        System.out.println("Печатаю истроию \n"+inMemoryTaskManager.getHistory().size()+" "+inMemoryTaskManager.getHistory().toString());
//        inMemoryTaskManager.getEpicTaskById(e);
//        inMemoryTaskManager.deleteEpicTaskById(epic1Id);
//        inMemoryTaskManager.deleteSubtaskById(3);
//        inMemoryTaskManager.deleteSubtaskById(4);
//        inMemoryTaskManager.deleteTaskById(1);
     //   inMemoryTaskManager.deleteTaskById(2);
        System.out.println("---------------------------------------------------------------------");

//        inMemoryTaskManager.deleteAllSubtask();

        System.out.println("Печатаю истроию \n"+inMemoryTaskManager.getHistory().size()+" "+inMemoryTaskManager.getHistory().toString());
    }
}