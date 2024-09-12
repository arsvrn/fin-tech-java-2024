package com.tbank.edu.hw3;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CustomLinkedListTest {

    @Test
    void add() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        assertEquals(3, list.size(), "The size of the list should be 3 after adding three items.");
        assertEquals(1, list.get(0), "The first element must be equal to 1.");
        assertEquals(3, list.get(2), "The third element must be equal to 3.");
    }

    @Test
    void get() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        list.add(10);
        list.add(20);

        assertEquals(10, list.get(0), "The first element must be equal to 10.");
        assertEquals(20, list.get(1), "The second element should be equal to 20.");
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(2), "Access to a non-existent element should raise an exception.");
    }

    @Test
    void remove() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        list.add(100);
        list.add(200);
        list.add(300);
        list.remove(1);

        assertEquals(2, list.size(), "The list size should be 2 after deleting one item.");
        assertEquals(100, list.get(0), "The first element must be equal to 100.");
        assertEquals(300, list.get(1), "The second element should be equal to 300.");
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(2), "The deleted item should not be available.");
    }

    @Test
    void contains() {
        CustomLinkedList<String> list = new CustomLinkedList<>();
        list.add("apple");
        list.add("banana");

        assertTrue(list.contains("apple"), "The list should contain 'apple'.");
        assertFalse(list.contains("cherry"), "The list should not contain 'cherry'.");
    }

    @Test
    void addAll() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        list.add(1);
        list.add(2);

        CustomLinkedList<Integer> additionalList = new CustomLinkedList<>();
        additionalList.add(3);
        additionalList.add(4);

        list.addAll(additionalList);

        assertEquals(4, list.size(), "The size of the list should be 4 after adding all the items.");
        assertEquals(3, list.get(2), "The third element must be equal to 3.");
        assertEquals(4, list.get(3), "The fourth element must be equal to 4.");
    }

    @Test
    void size() {
        CustomLinkedList<Double> list = new CustomLinkedList<>();
        assertEquals(0, list.size(), "The size of the empty list should be 0.");

        list.add(1.5);
        list.add(2.5);

        assertEquals(2, list.size(), "The size of the list should be 2 after adding two items.");
    }

    //Задание 2
    @Test
    void streamToCustomLinkedList() {
        Stream<Integer> stream = Stream.of(10, 20, 30, 40);

        CustomLinkedList<Integer> customList = stream.reduce(new CustomLinkedList<>(),
                (list, item) -> {
                    list.add(item);
                    return list;
                },
                (list1, list2) -> {
                    list1.addAll(list2);
                    return list1;
                });

        assertEquals(4, customList.size(), "The list size should be 4 after stream conversion.");
        assertEquals(10, customList.get(0), "The first element must be equal to 10.");
        assertEquals(20, customList.get(1), "The second element should be equal to 20.");
        assertEquals(30, customList.get(2), "The third element should be equal to 30.");
        assertEquals(40, customList.get(3), "The fourth element should be equal to 40.");
    }
}