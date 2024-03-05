package com.example.the_tarlords.data.photo;

import android.graphics.Color;

import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;

public class ColorGenerator {
    private static Stack<Integer> colors;
    private static Stack<Integer> recycler;

    public void RandomColor() {
        colors = new Stack<>();
        recycler = new Stack<>();
        recycler.addAll(Arrays.asList(
                0xffb44336, 0xffa91e63, 0xff9c27b0, 0xff573398,
                0xff3f51b5, 0xff1c77bd, 0xff049ade, 0xff009688,
                0xff418eff, 0xff79a841, 0xffe4ae08, 0xffdd8400,
                0xffcf5722, 0xff795548, 0xff607d8b, 0xff333333));
    }

    public static Integer getRandomColor() {
        if (colors.size() == 0) {
            while(!recycler.isEmpty()) {
                colors.push(recycler.pop());
            }
            Collections.shuffle(colors);
        }
        int c = colors.pop();
        recycler.push(c);
        return c;
    }

}
