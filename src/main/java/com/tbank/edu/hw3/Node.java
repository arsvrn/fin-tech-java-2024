package com.tbank.edu.hw3;

public class Node<T> {
    T data;
    Node<T> next;
    Node<T> prev;

    Node(T data) {
        this.data = data;
    }
}