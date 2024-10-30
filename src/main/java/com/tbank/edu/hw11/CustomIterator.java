package com.tbank.edu.hw11;

import java.util.function.Consumer;

public interface CustomIterator<T> {
    boolean hasNext();
    T next();
    void forEachRemaining(Consumer<? super T> actio);
}
