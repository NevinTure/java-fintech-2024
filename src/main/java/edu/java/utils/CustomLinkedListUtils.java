package edu.java.utils;

public class CustomLinkedListUtils {

    private CustomLinkedListUtils() {
    }

    public static <T> void copy(CustomLinkedList<? extends T> from, CustomLinkedList<? super T> to) {
        for (int i = 0; i < from.size(); i++) {
            to.add(from.get(i));
        }
    }
}
