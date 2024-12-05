package edu.java;

import java.util.*;

public class Main {

    private Main() {
    }

    public static void main(String[] args) {
        outOfMemoryExample();
    }

    public static void stackOverFlowExample() {
        stackOverFlowExample();
    }

    public static void outOfMemoryExample() {
        List<String> list = new ArrayList<>();
        while (true) {
            list.add("1".repeat(1_000_000_000));
        }
    }
}

