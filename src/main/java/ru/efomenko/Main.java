package ru.efomenko;

import ru.efomenko.model.EpicTask;
import ru.efomenko.model.Status;
import ru.efomenko.model.Subtask;
import ru.efomenko.model.Task;
import ru.efomenko.service.CanbanManager;

import java.util.List;

public class Main {
    static CanbanManager canbanManager;



    public static void main(String[] args) {
        canbanManager = new CanbanManager();

        Task task = new Task("Сходить в магазин","В магазине купить продукты",Status.STATUS.NEW);
        EpicTask epic = new EpicTask("Написать программу Канбан","Написать программу которая сохраняет " +
                "задачи и их делит на категории", Status.STATUS.NEW);
        EpicTask epic1 = new EpicTask("Написать класс менеджер","В классе менеджер будет сосредоточена " +
                "вся работа с задачами",Status.STATUS.NEW);
        Subtask subtask = new Subtask("Составить таблицу классов","",Status.STATUS.NEW);
        Subtask subtask1 = new Subtask("Составить список методов","",Status.STATUS.NEW);
        Subtask subtask2 = new Subtask("Запрограммировать методы","",Status.STATUS.NEW);

        canbanManager.createTask(task);
        canbanManager.createEpicTask(epic1);
        canbanManager.createEpicTask(epic);
        canbanManager.addSubtaskInEpicTask(epic1,subtask1);
        canbanManager.addSubtaskInEpicTask(epic1,subtask2);
        canbanManager.addSubtaskInEpicTask(epic,subtask);
        print();
        System.out.println("---------------------------------------------------------------------");
//        Task subtask3 =  canbanManager.getSubtaskById(4).putStatus(STATUS.DONE);
//        Task subtask4 = canbanManager.getSubtaskById(5).putStatus(STATUS.DONE);
//        canbanManager.putSubtask(epic1,(Subtask) subtask3); ;
        print();

       /* System.out.println("task = " + task);
        System.out.println("epic = " + epic);
        System.out.println("epic1 = " + epic1);*/
    }

    public static void print(){
       List<EpicTask> epicTaskHashMap = canbanManager.getEpicTaskList();
       List<Task> taskHashMap = canbanManager.getTaskList();
        for (EpicTask ep:epicTaskHashMap) {
            System.out.println("epicTaskHashMap = " + ep);
            System.out.println(canbanManager.getSubtaskListByEpicTaskId(ep.getId()));
        }

        for (Task taskKey : taskHashMap){
            System.out.println("taskHashMap = " + taskKey);
        }
    }

}
