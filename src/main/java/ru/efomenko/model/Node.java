package ru.efomenko.model;

public class  Node<E> {
    public Node<E> first;
    public Node<E> last;
    public E task;

    public Node(Node<E> first,E task, Node<E> last){
        this.first = first;
        this.task = task;
        this.last = last;
    }
}
