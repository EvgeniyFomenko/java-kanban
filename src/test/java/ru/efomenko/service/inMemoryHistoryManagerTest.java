package ru.efomenko.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.efomenko.model.EpicTask;
import ru.efomenko.model.Status;
import ru.efomenko.model.Subtask;
import ru.efomenko.model.Task;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class inMemoryHistoryManagerTest {
    HistoryManager historyManager ;

    @BeforeEach
    public void createObjects(){
        historyManager = new InMemoryHistoryManager();
    }
    @Test
    public void remove() {
        Task task = new Task("Сходить в магазин","В магазине купить продукты", Status.NEW);
        task.setId(4);
        EpicTask epic = new EpicTask("Написать программу Канбан","Написать программу которая сохраняет " +
                "задачи и их делит на категории", Status.NEW);
        epic.setId(1);
        EpicTask epic1 = new EpicTask("Написать класс менеджер","В классе менеджер будет сосредоточена " +
                "вся работа с задачами",Status.NEW);
        epic1.setId(2);

        Subtask subtask1 = new Subtask(1,"Составить список методов","description",Status.DONE);
        subtask1.setId(5);
        Subtask subtask2 = new Subtask(2,"Запрограммировать методы","description",Status.NEW);
        subtask2.setId(6);
        Subtask subtask = new Subtask(2,"Составить таблицу классов","",Status.NEW);
        subtask.setId(7);

        historyManager.add(epic1);
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);
        historyManager.add(subtask1);
        historyManager.add(subtask2);

        historyManager.remove(epic1.getId());
        assertEquals(5,historyManager.getHistory().size());

        historyManager.remove(subtask2.getId());
        assertEquals(4,historyManager.getHistory().size());

        historyManager.remove(epic.getId());
        assertEquals(3,historyManager.getHistory().size());

        historyManager.add(epic1);
        historyManager.add(epic1);
        assertEquals(4,historyManager.getHistory().size());

    }

    @Test
    void add() {
        Task task = new Task("Сходить в магазин","В магазине купить продукты", Status.NEW);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();

        assertNotNull(history, "История пустая.");
        assertEquals(1, history.size(), "История пустая.");
    }

    @Test
    void empty(){
        final List<Task> history = historyManager.getHistory();

        assertEquals(Collections.EMPTY_LIST,history);
    }
}