package org.example;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        int numElements = 100000;

        // Тестирование моей
        EgorMap<Integer, Integer> egorMap = new EgorMap<>();
        long startTime = System.nanoTime();
        for (int i = 0; i < numElements; i++) {
            egorMap.put(i, i);
        }
        long endTime = System.nanoTime();
        System.out.println("EgorMap put time: " + (endTime - startTime) / 1000000 + " ms");

        startTime = System.nanoTime();
        for (int i = 0; i < numElements; i++) {
            egorMap.get(i);
        }
        endTime = System.nanoTime();
        System.out.println("EgorMap get time: " + (endTime - startTime) / 1000000 + " ms");

        startTime = System.nanoTime();
        for (int i = 0; i < numElements; i++) {
            egorMap.remove(i);
        }
        endTime = System.nanoTime();
        System.out.println("EgorMap remove time: " + (endTime - startTime) / 1000000 + " ms");

        // Тестирование HashMap
        Map<Integer, Integer> hashMap = new HashMap<>();
        startTime = System.nanoTime();
        for (int i = 0; i < numElements; i++) {
            hashMap.put(i, i);
        }
        endTime = System.nanoTime();
        System.out.println("HashMap put time: " + (endTime - startTime) / 1000000 + " ms");

        startTime = System.nanoTime();
        for (int i = 0; i < numElements; i++) {
            hashMap.get(i);
        }
        endTime = System.nanoTime();
        System.out.println("HashMap get time: " + (endTime - startTime) / 1000000 + " ms");

        startTime = System.nanoTime();
        for (int i = 0; i < numElements; i++) {
            hashMap.remove(i);
        }
        endTime = System.nanoTime();
        System.out.println("HashMap remove time: " + (endTime - startTime) / 1000000 + " ms");
    }
}