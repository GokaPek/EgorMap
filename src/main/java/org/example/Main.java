package org.example;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<String , Integer> Mapa = new EgorMap();

        Mapa.put("Виталя", 1984);

        System.out.println(Mapa.get("Виталя"));
    }
}