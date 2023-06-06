package ru.efomenko.model;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private long id;
    private String name;
    private String text;
    private Status status;
    private Duration duration;
    private LocalDateTime startTime;

    public Task(String name, String text, Status status, LocalDateTime startTime, int duration) {
        this.name = name;
        this.text = text;
        this.status = status;
        this.startTime = startTime;
        this.duration = Duration.ofMinutes(duration);
    }

    public Task(String name, String text, Status status) {
        this.name = name;
        this.text = text;
        this.status = status;
        duration = Duration.ofMinutes(0);
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

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(text, task.text) && Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, text, status, duration, startTime);
    }

    public LocalDateTime getEndTime() {
        if (startTime == null) {
            return null;
        }
        return startTime.plus(duration);
    }

    public LocalDateTime getStartTime() {
        if (startTime == null) {
            return null;
        }
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(long duration) {
        this.duration = Duration.ofMinutes(duration);
    }

    public Duration getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", text='" + text + '\'' +
                ", status='" + status + '\'' +
                ", startTime='" + startTime + '\'' +
                ", duration='" + duration.toMinutes() + '\'' +
                '}';
    }
}
