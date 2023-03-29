package ru.efomenko.model;

import java.util.ArrayList;
import java.util.List;

public class EpicTask extends Task {

    private final List<Long> subTaskIdList;
    public EpicTask( String name, String text, Status.STATUS status) {
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
