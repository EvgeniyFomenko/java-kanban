package ru.efomenko;

import ru.efomenko.model.EpicTask;
import ru.efomenko.model.Status;
import ru.efomenko.model.Subtask;
import ru.efomenko.model.Task;
import ru.efomenko.service.FileBackedTasksManager;
import ru.efomenko.utility.Managers;

import java.io.File;

public class loadFromMemoryTest {

    public static void main(String[] args) {

        FileBackedTasksManager fileBackedTasksManager ;
        File file = new File("resources/fileSave.csv");
        fileBackedTasksManager = (FileBackedTasksManager) Managers.getFileBackedTaskManager(file);

        Task task = new Task("Сходить в магазин","В магазине купить продукты", Status.NEW);
        EpicTask epic = new EpicTask("Написать программу Канбан","Написать программу которая сохраняет " +
                "задачи и их делит на категории", Status.NEW);
        EpicTask epic1 = new EpicTask("Написать класс менеджер","В классе менеджер будет сосредоточена " +
                "вся работа с задачами",Status.NEW);
        long taskId = fileBackedTasksManager.createTask(task).getId();
        long epic1Id = fileBackedTasksManager.createEpicTask(epic1).getId();
        long epicId = fileBackedTasksManager.createEpicTask(epic).getId();
        Subtask subtask1 = new Subtask(epic1Id,"Составить список методов","description",Status.DONE);
        Subtask subtask2 = new Subtask(epic1Id,"Запрограммировать методы","description",Status.NEW);
        Subtask subtask = new Subtask(epicId,"Составить таблицу классов","",Status.NEW);

        fileBackedTasksManager.addSubtaskInEpicTask(subtask1);
        fileBackedTasksManager.addSubtaskInEpicTask(subtask2);
        fileBackedTasksManager.addSubtaskInEpicTask(subtask);
        fileBackedTasksManager.getEpicTaskById(epicId);
        fileBackedTasksManager.getEpicTaskById(epic1Id);
        fileBackedTasksManager.getTaskById(taskId);

        FileBackedTasksManager fileBackedTasksManager1 =(FileBackedTasksManager) Managers.getFileBackedTaskManager(file);

        System.out.println(fileBackedTasksManager1.getEpicTaskList());
        System.out.println(fileBackedTasksManager1.getSubtaskList());
        System.out.println(fileBackedTasksManager1.getTaskList());
        System.out.println(fileBackedTasksManager1.getHistory());
        fileBackedTasksManager1.deleteAllEpicTasks();
        fileBackedTasksManager1.deleteAllTasks();
//        fileBackedTasksManager1.deleteAllSubtask();

    }


}
