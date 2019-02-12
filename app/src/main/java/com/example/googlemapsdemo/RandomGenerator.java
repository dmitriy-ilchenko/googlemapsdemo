package com.example.googlemapsdemo;

import java.util.Random;

public class RandomGenerator {
    private static Random random = new Random();

    public static float getRandomFloat(float min, float max) {
        return random.nextFloat() * (max - min) + min;
    }
}
