package ru.efomenko.service;

import ru.efomenko.model.Node;
import ru.efomenko.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final CustomLinkedList<Task> customLinkedList;

    public InMemoryHistoryManager() {
        customLinkedList = new CustomLinkedList<>();
    }

    @Override
    public void add(Task task) {
        long id = task.getId();

        customLinkedList.linkLast(id, task);
    }

    @Override
    public List<Task> getHistory() {
        return customLinkedList.getTasks();
    }

    @Override
    public void remove(long id) {
        customLinkedList.remove(id);
    }

    public static class CustomLinkedList<E> {
        Map<Long, Node<E>> historyHashMap;
        Node<E> last;
        Node<E> first;

        public CustomLinkedList() {
            historyHashMap = new HashMap<>();
            last = null;
            first = null;
        }

        public void linkLast(Long id, E task) {
            Node<E> newNode = new Node<>(this.last, task, null);
            this.last = newNode;

            if (first == null) {
                this.first = newNode;
            }

            if (historyHashMap.containsKey(id)) {
                removeNode(historyHashMap.get(id));
                historyHashMap.remove(id);
            }
            historyHashMap.put(id, newNode);
        }

        public List<E> getTasks() {
            List<E> taskList = new ArrayList<>();

            for (Node<E> node : historyHashMap.values()) {
                taskList.add(node.task);
            }
            return taskList;
        }

        private void removeNode(Node<E> node) {
            Node<E> last = node.last;
            Node<E> first = node.first;

            if (last != null) {
                last.first = first;
            }

            if (first != null) {
                first.last = last;
            }
        }

        public void remove(Long id) {
            if (historyHashMap.get(id) != null) {
                removeNode(historyHashMap.get(id));
                historyHashMap.remove(id);
            }
        }
    }
}
