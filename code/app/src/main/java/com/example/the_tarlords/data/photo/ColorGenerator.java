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
                0xfff44336, 0xffe91e63, 0xff9c27b0, 0xff673ab7,
                0xff3f51b5, 0xff2196f3, 0xff03a9f4, 0xff009688,
                0xff4caf50, 0xff8bc34a, 0xffffc107, 0xffff9800,
                0xffff5722, 0xff795548, 0xff607d8b, 0xff333333));
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
