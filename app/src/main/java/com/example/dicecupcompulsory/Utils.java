package com.example.dicecupcompulsory;

import java.util.Random;

public class Utils {

    private final Random rand = new Random();

    public Utils() {
    }

    public int getRandomInt(int min, int max){
        return rand.nextInt((max - min) + 1) + min;
    }
}
