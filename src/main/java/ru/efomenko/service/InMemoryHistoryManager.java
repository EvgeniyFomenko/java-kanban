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

    public class CustomLinkedList<E> {
        Map<Long, Node<E>> historyHashMap;
        Node<E> last;
        Node<E> first;

        public CustomLinkedList() {
            historyHashMap = new HashMap<>();
            last = null;
            first = null;
        }

        public void linkLast(Long id, E task) {
            Node<E> n = new Node<>(this.last, task, null);
            this.last = n;

            if (first == null) {
                this.first = n;
            }

            if (historyHashMap.containsKey(id)) {
                removeNode(historyHashMap.get(id));
                historyHashMap.remove(id);
            }
            historyHashMap.put(id, n);
        }

        public List<E> getTasks() {
            List<E> taskList = new ArrayList<>();

            for (Node<E> node : historyHashMap.values()) {
                taskList.add(node.task);
            }
            return taskList;
        }

        public void removeNode(Node<E> node) {
            Node<E> l = node.last;
            Node<E> f = node.first;

            if (l != null) {
                l.first = f;
            }

            if (f != null) {
                f.last = l;
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
