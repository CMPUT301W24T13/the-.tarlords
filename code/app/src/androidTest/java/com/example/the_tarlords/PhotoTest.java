package com.example.the_tarlords;

import static org.junit.Assert.assertEquals;

import android.graphics.Bitmap;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.the_tarlords.data.photo.Photo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class PhotoTest {
    @Mock
    Bitmap mockBitmap;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testGetFileName() {
        String fileName = "test.jpg";
        Photo photo = new Photo(fileName, mockBitmap);
        assertEquals(fileName, photo.getFileName());
    }
}
