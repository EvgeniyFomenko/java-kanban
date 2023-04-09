package ru.efomenko.model;

import java.util.Objects;

public class Task {
    private long id;
    private String name;
    private String text;
    private Status status;

    public Task(String name, String text, Status status) {
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
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
