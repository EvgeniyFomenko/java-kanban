package ru.efomenko.exceptions;

public class ManagerSaveException extends RuntimeException{
    public ManagerSaveException (String text){
        super(text);
    }
}
