package ru.efomenko.model;

import java.util.ArrayList;
import java.util.List;

public class EpicTask extends Task {

    private final List<Long> subTaskIdList;
    public EpicTask( String name, String text, Status status) {
        super(name, text,status);
        subTaskIdList = new ArrayList<>();
    }

    public void deleteSubtaskById(long id){
        subTaskIdList.remove(id);
    }

    public List<Long> getSubTaskIdList() {
        return subTaskIdList;
    }

    public void addSubTaskId(Long id){
        this.subTaskIdList.add(id);
    }

    public void deleteAllSubtask(){
        subTaskIdList.removeAll(subTaskIdList);
    }

    @Override
    public String toString() {
        return "EpicTask{" +
                super.toString()+
                " subTaskIdList=" + subTaskIdList +
                '}';
    }
}
