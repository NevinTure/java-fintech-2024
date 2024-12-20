package edu.java.utils;

import lombok.*;
import java.util.Collection;

@SuppressWarnings("ParameterAssignment")
public class CustomLinkedList<T> {

    private final Node<T> head;
    private final Node<T> tail;
    private int size;

    public CustomLinkedList() {
        head = new Node<>();
        tail = new Node<>();
        head.setNext(tail);
        tail.setPrev(head);
        size = 0;
    }

    public T get(int index) {
        if (!checkIndex(index)) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> now = head.getNext();
        while (index > 0) {
            now = now.getNext();
            index--;
        }
        return now.getVal();
    }

    public boolean add(T t) {
        Node<T> prev = tail.getPrev();
        Node<T> now = new Node<>(t, tail, prev);
        prev.setNext(now);
        tail.setPrev(now);
        size++;
        return true;
    }

    public T remove(int index) {
        if (!checkIndex(index)) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> now = head.getNext();
        while (index > 0) {
            now = now.getNext();
            index--;
        }
        unlink(now);
        return now.getVal();
    }

    public boolean remove(Object o) {
        if (o == null) {
            Node<T> now = head.getNext();
            while (now != tail) {
                if (now.getVal() == null) {
                    unlink(now);
                    return true;
                }
                now = now.getNext();
            }
        } else {
            Node<T> now = head.getNext();
            while (now != tail) {
                if (o.equals(now.getVal())) {
                    unlink(now);
                    return true;
                }
                now = now.getNext();
            }
        }
        return false;
    }

    public boolean contains(Object o) {
        if (o == null) {
            Node<T> now = head.getNext();
            while (now != tail) {
                if (now.getVal() == null) {
                    return true;
                }
                now = now.getNext();
            }
        } else {
            Node<T> now = head.getNext();
            while (now != tail) {
                if (o.equals(now.getVal())) {
                    return true;
                }
                now = now.getNext();
            }
        }
        return false;
    }

    public boolean addAll(Collection<? extends T> c) {
        if (c == null || c.isEmpty()) {
            return false;
        }
        size += c.size();
        Node<T> now = tail.getPrev();
        for (T t : c) {
            Node<T> temp = new Node<>(t, null, now);
            now.setNext(temp);
            now = temp;
        }
        now.setNext(tail);
        tail.setPrev(now);
        return true;
    }

    public int size() {
        return size;
    }

    private boolean checkIndex(int index) {
        return index >= 0 && index < size;
    }

    private void unlink(Node<T> node) {
        Node<T> prev = node.getPrev();
        Node<T> next = node.getNext();
        prev.setNext(next);
        next.setPrev(prev);
        size--;
    }

    public CustomIterator<T> iterator() {
        return new CustomIterator<>(this);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Node<V> {
        private V val;
        private Node<V> next;
        private Node<V> prev;
    }
}
