package ru.efomenko.model;

import ru.efomenko.Main;

import java.util.Objects;

public class Subtask extends Task {
    private long idEpic;

    public Subtask(String name, String text, Main.STATUS status) {
        super(name, text,status);
    }

    public void setIdEpic(long idEpic){
        this.idEpic = idEpic;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idEpic);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "idEpic=" + idEpic + super.toString()+
                '}';
    }
}
