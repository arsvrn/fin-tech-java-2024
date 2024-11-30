package com.tbank.edu.hw16;

public class StackOverflowExample {
    public static void main(String[] args) {
        stackOverflow();
    }

    private static void stackOverflow() {
        stackOverflow();
    }
}
