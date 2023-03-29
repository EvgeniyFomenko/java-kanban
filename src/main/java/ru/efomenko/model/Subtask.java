package ru.efomenko.model;

import java.util.Objects;

public class Subtask extends Task {
    private long idEpic;

    public Subtask(String name, String text, Status.STATUS status) {
        super(name, text,status);
    }

    public void setIdEpic(long idEpic){
        this.idEpic = idEpic;
    }

    public long getIdEpic(){
        return idEpic;
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
