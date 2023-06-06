package ru.efomenko.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    private long epicId;

    public Subtask(long epicId, String name, String text, Status status, LocalDateTime startTime, int duration) {
        super(name, text, status, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(long epicId, String name, String text, Status status) {
        super(name, text, status);
        this.epicId = epicId;
    }

    public void setEpicId(long epicId) {
        this.epicId = epicId;
    }

    public long getEpicId() {
        return epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "idEpic=" + epicId + super.toString() +
                '}';
    }
}
