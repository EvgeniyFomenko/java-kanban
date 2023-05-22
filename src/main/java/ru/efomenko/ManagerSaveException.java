package ru.efomenko;

public class ManagerSaveException extends RuntimeException{
    public ManagerSaveException (String text){
        super(text);
    }
}
