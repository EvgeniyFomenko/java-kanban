package ru.efomenko.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EpicTask extends Task {

    private final List<Long> subTaskIdList;
    private LocalDateTime endTime;

    public EpicTask(String name, String text, Status status,LocalDateTime startTime, int duration){
        super(name, text, status, startTime, duration);
        subTaskIdList = new ArrayList<>();
    }

    public EpicTask( String name, String text, Status status) {
        super(name, text, status);
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
        subTaskIdList.clear();
    }

    public LocalDateTime getEndTime(){
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime){
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "EpicTask{" +
                super.toString()+
                "endTime="+ endTime+
                " subTaskIdList=" + subTaskIdList +
                '}';
    }
}
