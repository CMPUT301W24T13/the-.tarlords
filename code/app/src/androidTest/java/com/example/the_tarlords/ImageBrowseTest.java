package com.example.the_tarlords;

import static org.junit.Assert.assertEquals;


import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.the_tarlords.data.photo.Photo;
import com.example.the_tarlords.ui.image.ImageBrowseFragment;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

/**
 * Tests the UI for the image browse fragment
 */

@RunWith(AndroidJUnit4.class)
public class ImageBrowseTest {
    @Test
    public void testGetImages() {
        // Create a mock ArrayList of Photo objects
        ArrayList<Photo> mockImages = new ArrayList<>();
        mockImages.add(new Photo("testImageData1","testCollection1", "testName1", "testDocId1" ));
        mockImages.add(new Photo("testImageData2","testCollection2", "testName2", "testDocId2" ));

        // Create an instance of ImageBrowseFragment
        ImageBrowseFragment fragment = new ImageBrowseFragment();

        // Set the mock ArrayList using the setter method
        fragment.setImages(mockImages);

        // Get the ArrayList using the getter method
        ArrayList<Photo> retrievedImages = fragment.getImages();

        // Assert that the retrieved ArrayList is equal to the mock ArrayList
        assertEquals(mockImages, retrievedImages);
    }
    @Test
    public void testSetImages() {
        ImageBrowseFragment fragment = new ImageBrowseFragment();
        ArrayList<Photo> testImages = new ArrayList<>();
        testImages.add(new Photo("testImageData1","testCollection1", "testName1", "testDocId1" ));
        testImages.add(new Photo("testImageData2","testCollection2", "testName2", "testDocId2" ));

        fragment.setImages(testImages);
        assertEquals(testImages, fragment.getImages());
    }

}
