package com.example.the_tarlords.data.photo;

import java.util.Random;

public class ColorGenerator {
    private static Integer[] colors = {0xFFFFC0CB, 0xFFDB7093, 0xFFADD8E6, 0xFF90EE90, 0xFFFFDAB9,
            0xFFE6E6FA, 0xFF87CEEB, 0xFF98FB98, 0xFFD8BFD8};

    public static int getRandomColor() {
        Random rand = new Random();
        int randInt = rand.nextInt(colors.length);

        int c = colors[randInt];
        return c;
    }

}
