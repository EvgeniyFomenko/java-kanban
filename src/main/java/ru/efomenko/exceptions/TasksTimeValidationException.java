package ru.efomenko.exceptions;

public class TasksTimeValidationException extends IllegalArgumentException {
    public TasksTimeValidationException(String exception){
        super(exception);
    }
}
