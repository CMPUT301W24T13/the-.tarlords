package com.example.the_tarlords.data.photo;

import java.util.Random;

public class ColorGenerator {

    private static Integer[] colors = {0xFFFFC0CB, 0xFFDB7093, 0xFFADD8E6, 0xFF90EE90, 0xFFFFDAB9, 0xFFE6E6FA, 0xFF87CEEB, 0xFF98FB98, 0xFFD8BFD8};
    /*
    public void RandomColor() {
        colors = new Stack<>();
        recycler = new Stack<>();
        recycler.addAll(Arrays.asList(
                0xFFB44336, 0xFFA91E63, 0xFF9C27B0, 0xFF573398,
                0xFF3F51B5, 0xFF1C77BD, 0xFF049ADE, 0xFF009688,
                0xFF418EFF, 0xFF79A841, 0xFFE4AE08, 0xFFDD8400,
                0xFFCF5722, 0xFF795548, 0xFF607D8B, 0xFF333333)
        );
    }*/

    public static int getRandomColor() {
        Random rand = new Random();
        int randInt = rand.nextInt(colors.length);

        int c = colors[randInt];
        return c;
    }

}
