package ru.efomenko;

import ru.efomenko.model.EpicTask;
import ru.efomenko.model.Status;
import ru.efomenko.model.Subtask;
import ru.efomenko.model.Task;
import ru.efomenko.service.InMemoryTaskManager;
import ru.efomenko.utility.Managers;

import java.util.List;

public class Main {
    static InMemoryTaskManager inMemoryTaskManager;

    public static void main(String[] args) {
        inMemoryTaskManager =(InMemoryTaskManager) Managers.getDefault();

        Task task = new Task("Сходить в магазин","В магазине купить продукты",Status.NEW);
        EpicTask epic = new EpicTask("Написать программу Канбан","Написать программу которая сохраняет " +
                "задачи и их делит на категории", Status.NEW);
        EpicTask epic1 = new EpicTask("Написать класс менеджер","В классе менеджер будет сосредоточена " +
                "вся работа с задачами",Status.NEW);


        inMemoryTaskManager.createTask(task);
        long epic1Id = inMemoryTaskManager.createEpicTask(epic1).getId();
        long epicId = inMemoryTaskManager.createEpicTask(epic).getId();
        Subtask subtask = new Subtask(epicId,"Составить таблицу классов","",Status.NEW);
        Subtask subtask1 = new Subtask(epic1Id,"Составить список методов","",Status.DONE);
        Subtask subtask2 = new Subtask(epic1Id,"Запрограммировать методы","",Status.NEW);
        inMemoryTaskManager.addSubtaskInEpicTask(subtask1);
        inMemoryTaskManager.addSubtaskInEpicTask(subtask2);
        inMemoryTaskManager.addSubtaskInEpicTask(subtask);
        System.out.println(inMemoryTaskManager.getHistory().size()+" "+inMemoryTaskManager.getHistory().toString());
        print();
        System.out.println("---------------------------------------------------------------------");
//        Task subtask3 =  canbanManager.getSubtaskById(4).putStatus(STATUS.DONE);
//        Task subtask4 = canbanManager.getSubtaskById(5).putStatus(STATUS.DONE);
//        canbanManager.putSubtask(epic1,(Subtask) subtask3); ;

        inMemoryTaskManager.deleteAllSubtask();
        print();
        System.out.println(inMemoryTaskManager.getHistory().size()+" "+inMemoryTaskManager.getHistory().toString());
       /* System.out.println("task = " + task);
        System.out.println("epic = " + epic);
        System.out.println("epic1 = " + epic1);*/
    }

    public static void print(){
       List<EpicTask> epicTaskHashMap = inMemoryTaskManager.getEpicTaskList();
       List<Task> taskHashMap = inMemoryTaskManager.getTaskList();
        for (EpicTask ep:epicTaskHashMap) {
            System.out.println("epicTaskHashMap = " + ep);
            System.out.println(inMemoryTaskManager.getSubtaskListByEpicTaskId(ep.getId()));
        }

        for (Task taskKey : taskHashMap){
            System.out.println("taskHashMap = " + taskKey);
        }
    }

}
