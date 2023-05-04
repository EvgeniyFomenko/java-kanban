package ru.efomenko.model;

public class  Node<E> {
    public Node first;
    public Node last;
    public E task;

    public Node(Node first,E task, Node last){
        this.first = first;
        this.task = task;
        this.last = last;
    }
}
