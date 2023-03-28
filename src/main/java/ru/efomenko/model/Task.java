package ru.efomenko.model;

import ru.efomenko.Main;

import java.util.Objects;

public class Task {
    long id;
    String name;
    String text;
    Main.STATUS status;

    public Task(String name, String text, Main.STATUS status) {
        this.name = name;
        this.text = text;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Main.STATUS getStatus() {
        return status;
    }

    public void setStatus(Main.STATUS status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(text, task.text)&&Objects.equals(status,task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, text, status);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", text='" + text + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
