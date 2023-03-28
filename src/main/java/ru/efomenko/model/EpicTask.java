package ru.efomenko.model;

import ru.efomenko.Main;

import java.util.ArrayList;
import java.util.List;

public class EpicTask extends Task {

    List<Long> subTaskIdList;
    public EpicTask( String name, String text, Main.STATUS status) {
        super(name, text,status);
        subTaskIdList = new ArrayList<>();
    }

    public List<Long> getSubTaskIdList() {
        return subTaskIdList;
    }

    public void addSubTaskId(Long id){
        this.subTaskIdList.add(id);
    }
}
