package ru.efomenko;

import ru.efomenko.service.FileBackedTasksManager;

import java.io.File;

public class loadFromMemoryTest {

    public static void main(String[] args) {
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(new File("rescources/fileSave.csv"));

        System.out.println(fileBackedTasksManager.getHistory());
        System.out.println(fileBackedTasksManager.getTaskList());
        System.out.println(fileBackedTasksManager.getEpicTaskList());
        System.out.println(fileBackedTasksManager.getSubtaskList());

    }


}
