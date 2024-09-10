package com.tbank.edu.hw3;

class CustomLinkedList<T> implements ListInterface<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    public CustomLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    @Override
    public void add(T value) {
        Node<T> newNode = new Node<>(value);
        if (head == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    @Override
    public T get(int index) {
        checkIndex(index);
        Node<T>  current = getNode(index);
        return current.data;
    }

    @Override
    public void remove(int index) {
        checkIndex(index);
        Node<T>  toRemove = getNode(index);

        if (toRemove == head) {
            head = head.next;
            if (head != null) {
                head.prev = null;
            } else {
                tail = null;
            }
        } else if (toRemove == tail) {
            tail = tail.prev;
            tail.next = null;
        } else {
            toRemove.prev.next = toRemove.next;
            toRemove.next.prev = toRemove.prev;
        }
        size--;
    }

    @Override
    public boolean contains(T value) {
        Node<T>  current = head;
        while (current != null) {
            if (current.data.equals(value)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public void addAll(ListInterface<T> list) {
        for (int i = 0; i < list.size(); i++) {
            add(list.get(i));
        }
    }

    private Node<T>  getNode(int index) {
        Node<T>  current;
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return current;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    @Override
    public int size() {
        return size;
    }
}
