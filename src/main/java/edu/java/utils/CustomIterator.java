package edu.java.utils;

import java.util.*;
import java.util.function.Consumer;

public class CustomIterator<T> {

    private final CustomLinkedList<T> list;
    private int cursor = 0;

    public CustomIterator(CustomLinkedList<T> list) {
        CustomLinkedList<T> copy = new CustomLinkedList<>();
        CustomLinkedListUtils.copy(list, copy);
        this.list = copy;
    }

    public boolean hasNext() {
        return cursor < list.size();
    }

    public T next() {
        if (hasNext()) {
            return list.get(cursor++);
        }
        throw new NoSuchElementException();
    }

    public void forEachRemaining(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        while (hasNext()) {
            action.accept(next());
        }
    }
}
