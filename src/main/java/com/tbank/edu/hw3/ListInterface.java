package com.tbank.edu.hw3;

public interface ListInterface<T> {
    void add(T value);

    T get(int index);

    void remove(int index);

    boolean contains(T value);

    void addAll(ListInterface<T> list);

    int size();
}
